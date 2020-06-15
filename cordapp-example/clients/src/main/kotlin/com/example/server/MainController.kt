package com.example.server

import com.example.flow.ExampleFlow.Initiator
import com.example.state.IOUState
import com.example.excel.exportExcel
import net.corda.core.contracts.StateAndRef
import net.corda.core.identity.CordaX500Name
import net.corda.core.messaging.startTrackedFlow
import net.corda.core.messaging.vaultQueryBy
import net.corda.core.utilities.getOrThrow
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType.*
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpServletRequest

val SERVICE_NAMES = listOf("Notary", "Network Map Service")
val screenpeers_reverse = mapOf("O=ChangQing, L=XiAn, C=CN" to "长庆钻井总公司","O=CompanyA, L=ChengDu, C=CN" to "陕西银邦","O=CompanyB, L=ShangHai, C=CN" to "宁夏恒顺")
/**
 *  A Spring Boot Server API controller for interacting with the node via RPC.
 */

@RestController
@RequestMapping("/api/example/") // The paths for GET and POST requests are relative to this base path.
class MainController(rpc: NodeRPCConnection) {

    companion object {
        private val logger = LoggerFactory.getLogger(RestController::class.java)
    }

    private val myLegalName = rpc.proxy.nodeInfo().legalIdentities.first().name
    private val proxy = rpc.proxy

    /**
     * Returns the node's name.
     */
    @GetMapping(value = [ "me" ], produces = [ APPLICATION_JSON_VALUE ])
    fun whoami() = mapOf("me" to myLegalName)

    /**
     * Returns all parties registered with the network map service. These names can be used to look up identities using
     * the identity service.
     */
    @GetMapping(value = [ "peers" ], produces = [ APPLICATION_JSON_VALUE ])
//    fun getPeers(): Map<String, List<CordaX500Name>> {
    fun getPeers(): Map<String, List<String>> {
        val nodeInfo = proxy.networkMapSnapshot()
        val peers = nodeInfo.map { it.legalIdentities.first().name }.filter { it.organisation !in (SERVICE_NAMES + myLegalName.organisation) }
        val peerList = mutableListOf("1")
        peerList.clear()
        for (element in peers) {
//            println(element)
//            println(screenpeers_reverse.getValue("$element"))
//            val peer = screenpeers_reverse.getValue("$element")
//            println(peer)
            peerList.add(screenpeers_reverse.getValue("$element"))
        }
        return mapOf("peers" to peerList)
//        return mapOf("peers" to nodeInfo
//                .map { it.legalIdentities.first().name }
//                //filter out myself, notary and eventual network map started by driver
//                .filter { it.organisation !in (SERVICE_NAMES + myLegalName.organisation) })
    }

    /**
     * Displays all IOU states that exist in the node's vault.
     */
    @GetMapping(value = [ "ious" ], produces = [ APPLICATION_JSON_VALUE ])
    fun getIOUs() : List<Map<String,String>> {
//        return ResponseEntity.ok(proxy.vaultQueryBy<IOUState>().states)
        val myious = proxy.vaultQueryBy<IOUState>().states

        val iouList:List<Map<String,String>> = listOf()
        val mutableiouList = iouList.toMutableList()
        for (element in myious) {
            val iou = mutableMapOf("1" to "1")
            iou.clear()
            val seller = element.state.data.seller.toString()
            val buyer = element.state.data.buyer.toString()
            val oil = element.state.data.getoilValue()
            val cash = element.state.data.getcashValue()
            iou.put("seller",screenpeers_reverse.getValue(seller))
            iou.put("buyer",screenpeers_reverse.getValue(buyer))
//            println(screenpeers_reverse.getValue(seller))
            iou.put("oil",oil.toString())
            iou.put("cash",cash.toString())
            mutableiouList.add(iou)

        }
//        println(iouList)
//        return ResponseEntity.ok(myious)
        return mutableiouList
    }

    @GetMapping(value = [ "states" ], produces = [ APPLICATION_JSON_VALUE ])
    fun getStates() : ResponseEntity<List<StateAndRef<IOUState>>>  {
        return ResponseEntity.ok(proxy.vaultQueryBy<IOUState>().states)
    }

    /**
     * Initiates a flow to agree an IOU between two parties.
     *
     * Once the flow finishes it will have written the IOU to ledger. Both the lender and the borrower will be able to
     * see it when calling /spring/api/ious on their respective nodes.
     *
     * This end-point takes a Party name parameter as part of the path. If the serving node can't find the other party
     * in its network map cache, it will return an HTTP bad request.
     *
     * The flow is invoked asynchronously. It returns a future when the flow's call() method returns.
     */

    @PostMapping(value = [ "create-iou" ], produces = [ TEXT_PLAIN_VALUE ], headers = [ "Content-Type=application/x-www-form-urlencoded" ])
    fun createIOU(request: HttpServletRequest): ResponseEntity<String> {
        val cashValue = request.getParameter("cashValue").toInt()
        val oilValue = request.getParameter("oilValue").toInt()
        val partyName = request.getParameter("partyName")
        if(partyName == null){
            return ResponseEntity.badRequest().body("Query parameter 'partyName' must not be null.\n")
        }
        if (cashValue <= 0 ) {
            return ResponseEntity.badRequest().body("Query parameter 'cashValue' must be non-negative.\n")
        }
        val partyX500Name = CordaX500Name.parse(partyName)
        val otherParty = proxy.wellKnownPartyFromX500Name(partyX500Name) ?: return ResponseEntity.badRequest().body("Party named $partyName cannot be found.\n")

//        val exportexcel = exportExcel()
//        exportexcel.Test()

        return try {
            val signedTx = proxy.startTrackedFlow(::Initiator,cashValue,oilValue,otherParty).returnValue.getOrThrow()
            ResponseEntity.status(HttpStatus.CREATED).body("交易 ${signedTx.id} 成功提交到区块链中.\n")

        } catch (ex: Throwable) {
            logger.error(ex.message, ex)
            ResponseEntity.badRequest().body(ex.message!!)
        }

//        exportexcel.test()
    }

    @PostMapping(value = [ "export-excel" ], produces = [ TEXT_PLAIN_VALUE ], headers = [ "Content-Type=application/x-www-form-urlencoded" ])
    fun exportExcel(request: HttpServletRequest): ResponseEntity<String> {
        val buyer = request.getParameter("buyer")
        val seller = request.getParameter("seller")
        val cash = request.getParameter("cash")
        val oil = request.getParameter("oil")
        val hash = request.getParameter("hash")
        val dataarray = arrayOf(buyer,seller,cash,oil,hash)
//        val dataList = listOf(dataarray)

        try {
            val exportexcel = exportExcel()
            val result :Boolean = exportexcel.Test(dataarray)
            if (result) {
                return ResponseEntity.status(HttpStatus.CREATED).body("已经成功导入到表 C:\\corda\\report.xls\n")
            } else {
                return ResponseEntity.status(HttpStatus.CREATED).body("请再试一次\n")
            }
        } catch (ex: Throwable) {
            logger.error(ex.message, ex)
            return ResponseEntity.badRequest().body(ex.message!!)
        }
//        return try {
//            val exportexcel = exportExcel()
//
//
//            ResponseEntity.status(HttpStatus.CREATED).body("已经成功导入到表 C:\\excel\\report.xls\n")
//
//        } catch (ex: Throwable) {
//            logger.error(ex.message, ex)
//            ResponseEntity.badRequest().body(ex.message!!)
//        }
    }
    /**
     * Displays all IOU states that only this node has been involved in.
     */
    @GetMapping(value = [ "my-ious" ], produces = [ APPLICATION_JSON_VALUE ])
//    fun getMyIOUs(): ResponseEntity<List<StateAndRef<IOUState>>>  {
    fun getMyIOUs(): List<Map<String,String>> {
//    fun getMyIOUs(): List<List<String>> {
        val myious = proxy.vaultQueryBy<IOUState>().states.filter { it.state.data.buyer.equals(proxy.nodeInfo().legalIdentities.first()) }
        val iouList:List<Map<String,String>> = listOf()
        val mutableiouList = iouList.toMutableList()
//        iouList.clear()
        for (element in myious) {
            val iou = mutableMapOf("1" to "1")
            iou.clear()
            val seller = element.state.data.seller.toString()
            val buyer = element.state.data.buyer.toString()
            val oil = element.state.data.getoilValue()
            val cash = element.state.data.getcashValue()
            iou.put("seller",screenpeers_reverse.getValue(seller))
            iou.put("buyer",screenpeers_reverse.getValue(buyer))
            iou.put("oil",oil.toString())
            iou.put("cash",cash.toString())
            mutableiouList.add(iou)
//            println(mutableiouList)

//            val iou = element.state.data
//            iou.setchSeller(screenpeers_reverse.getValue("$element.state.data.seller"))
//            iou.setchBuyer(screenpeers_reverse.getValue("$element.state.data.buyer"))
//            println(screenpeers_reverse.getValue("$element.state.data.seller"))

//            element.state.data[buyer] = screenpeers_reverse.getValue("$element")
        }
//        println(mutableiouList)
//        return ResponseEntity.ok(myious)
        return mutableiouList
    }

}

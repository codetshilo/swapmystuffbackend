package com.swapmystuffbackend.controllers

import com.swapmystuffbackend.data.models.*
import com.swapmystuffbackend.dto.SwapApiResponse
import com.swapmystuffbackend.services.SwapService
import com.swapmystuffbackend.services.SwapRequestService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/swaps")
class SwapController(
    private val swapService: SwapService,
    private val swapRequestService: SwapRequestService
) {

    @GetMapping
    fun getSwaps(): ResponseEntity<List<SwapData>> {
        val swaps = swapService.getAllSwaps()
        return ResponseEntity.ok(swaps)
    }

    @PostMapping("/requests")
    fun initiateSwap(@RequestBody swapRequest: SwapRequest): ResponseEntity<SwapApiResponse> {
        val createdRequest = swapRequestService.initiateSwapRequest(swapRequest)
        return if (createdRequest != null) {
            val swapData = swapService.createSwapFromRequest(createdRequest)
            ResponseEntity.ok(SwapApiResponse(swapData.id, null, true, "Swap initiated"))
        } else {
            ResponseEntity.badRequest().body(SwapApiResponse("", null, false, "Failed to initiate swap"))
        }
    }

    @PostMapping("/request-product")
    fun postSwapRequest(@RequestBody product: Product): ResponseEntity<SwapApiResponse> {
        val swapRequest = SwapRequest(
            userId = product.ownerId, // Removed ?: "unknown"
            ownerId = product.ownerId, // Removed ?: "unknown"
            swapWithUserId = "unknown", // Placeholder: needs actual context
            selectedProductId = product.id ?: 0L, // Kept due to nullable id
            swapWithProductId = 0L, // Placeholder: needs actual context
            status = SwapRequestStatus.PENDING
        )
        val createdRequest = swapRequestService.initiateSwapRequest(swapRequest)
        return if (createdRequest != null) {
            ResponseEntity.ok(SwapApiResponse(createdRequest.id.toString(), null, true, "Swap request created"))
        } else {
            ResponseEntity.badRequest().body(SwapApiResponse("", null, false, "Failed to create swap request"))
        }
    }

    @GetMapping("/user/{userId}/products")
    fun getSwapsByUserProducts(@PathVariable userId: String): ResponseEntity<List<SwapData>> {
        val swaps = swapService.getSwapsByUser(userId)
        return ResponseEntity.ok(swaps)
    }

    @PostMapping
    fun createSwap(@RequestBody swapData: SwapData): ResponseEntity<SwapResponse> {
        val swap = swapService.createSwap(swapData)
        return ResponseEntity.ok(SwapResponse(swap.id))
    }

    @GetMapping("/{swapId}")
    fun getSwap(@PathVariable swapId: String): ResponseEntity<SwapResponse> {
        val swap = swapService.getSwapById(swapId)
        return if (swap != null) {
            ResponseEntity.ok(SwapResponse(swap.id))
        } else {
            ResponseEntity.notFound().build()
        }
    }

    @PutMapping("/{swapId}/accept")
    fun acceptSwap(@PathVariable swapId: String): ResponseEntity<SwapApiResponse> {
        val success = swapService.acceptSwap(swapId)
        return if (success) {
            ResponseEntity.ok(SwapApiResponse(swapId, null, true, "Swap accepted"))
        } else {
            ResponseEntity.badRequest().body(SwapApiResponse(swapId, null, false, "Swap not found or already accepted"))
        }
    }

    @PutMapping("/{swapId}/accept-products")
    fun acceptProductsInSwap(@PathVariable swapId: String): ResponseEntity<SwapApiResponse> {
        val success = swapService.acceptProductInSwap(swapId)
        return if (success) {
            ResponseEntity.ok(SwapApiResponse(swapId, null, true, "Products accepted in swap"))
        } else {
            ResponseEntity.badRequest().body(SwapApiResponse(swapId, null, false, "Failed to accept products"))
        }
    }

    @PutMapping("/{swapId}/reject")
    fun rejectSwap(@PathVariable swapId: String): ResponseEntity<SwapApiResponse> {
        val swap = swapService.getSwapById(swapId)
        return if (swap != null && swap.status == SwapStatus.PENDING) {
            swapService.updateSwapStatus(swapId, SwapStatus.REJECTED.name)
            ResponseEntity.ok(SwapApiResponse(swapId, null, true, "Swap rejected"))
        } else {
            ResponseEntity.badRequest().body(SwapApiResponse(swapId, null, false, "Swap not found or cannot be rejected"))
        }
    }

    @GetMapping("/user/{userId}")
    fun getSwapsByUser(@PathVariable userId: String): ResponseEntity<List<SwapData>> {
        val swaps = swapService.getSwapsByUser(userId)
        return ResponseEntity.ok(swaps)
    }

    @GetMapping("/pending")
    fun getPendingSwaps(): ResponseEntity<List<SwapData>> {
        val pendingSwaps = swapService.getPendingSwaps()
        return ResponseEntity.ok(pendingSwaps)
    }

    @GetMapping("/completed")
    fun getCompletedSwaps(): ResponseEntity<List<SwapData>> {
        val completedSwaps = swapService.getCompletedSwaps()
        return ResponseEntity.ok(completedSwaps)
    }

    @PutMapping("/{swapId}/status/{status}")
    fun updateSwapStatus(
        @PathVariable swapId: String,
        @PathVariable status: String
    ): ResponseEntity<SwapApiResponse> {
        val success = swapService.updateSwapStatus(swapId, status)
        return if (success) {
            ResponseEntity.ok(SwapApiResponse(swapId, null, true, "Status updated to $status"))
        } else {
            ResponseEntity.badRequest().body(SwapApiResponse(swapId, null, false, "Failed to update status"))
        }
    }

    @GetMapping("/{swapId}/messages")
    fun getSwapMessages(@PathVariable swapId: String): ResponseEntity<List<Message>> {
        val messages = swapService.getMessagesForSwap(swapId)
        return ResponseEntity.ok(messages)
    }

    @PostMapping("/{swapId}/messages")
    fun sendSwapMessage(
        @PathVariable swapId: String,
        @RequestBody message: Message
    ): ResponseEntity<SwapApiResponse> {
        val savedMessage = swapService.sendMessage(swapId, message)
        return if (savedMessage != null) {
            ResponseEntity.ok(SwapApiResponse(swapId, null, true, "Message sent"))
        } else {
            ResponseEntity.badRequest().body(SwapApiResponse(swapId, null, false, "Failed to send message"))
        }
    }
}
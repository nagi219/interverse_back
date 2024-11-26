//package com.interverse.demo.controller;
//
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RestController;
//
//import com.stripe.model.Event;
//import com.stripe.net.Webhook;
//
//import jakarta.servlet.http.HttpServletRequest;
//
//@RestController
//public class WebhookController {
//	
//	@PostMapping("/webhook")
//    public ResponseEntity<String> handleWebhook(@RequestBody String payload, HttpServletRequest request) {
//        String secret = "your_webhook_secret_here";
//
//        try {
//            Event event = Webhook.constructEvent(payload, request.getHeader("Stripe-Signature"), secret);
//
//            // Handle the event
//            switch (event.getType()) {
//                case "payment_intent.succeeded":
//                    // Handle successful payment
//                    break;
//                case "payment_intent.payment_failed":
//                    // Handle failed payment
//                    break;
//                // Add more cases for other event types as needed
//                default:
//                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Unhandled event type: " + event.getType());
//            }
//
//            return ResponseEntity.ok("Webhook handled: " + event.getType());
////        } catch (IOException e) {
////            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Webhook error: " + e.getMessage());
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Webhook error: " + e.getMessage());
//        }
//    }
//}

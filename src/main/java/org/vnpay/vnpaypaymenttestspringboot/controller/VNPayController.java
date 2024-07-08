package org.vnpay.vnpaypaymenttestspringboot.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.vnpay.vnpaypaymenttestspringboot.config.VNPayService;

@Controller
@RequiredArgsConstructor
public class VNPayController {

        private final VNPayService vnpayService;

        @GetMapping({"", "/"})
        public String home(){
            return "index";
        }

        @PostMapping("/submitOrder")
        public String submitOrder(@RequestParam("amount") int orderTotal,
                                  @RequestParam("orderInfo") String orderInfo,
                                  HttpServletRequest request){
            String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
            String vnPayUrl = vnpayService.createOrder(request, orderTotal, orderInfo, baseUrl);
            return "redirect:" + vnPayUrl;
        }


        @GetMapping("/vnpay-payment-return")
        public String paymentCompleted(HttpServletRequest request, Model model){
            int paymentStatus =vnpayService.orderReturn(request);

            String orderInfo = request.getParameter("vnp_OrderInfo");
            String paymentTime = request.getParameter("vnp_PayDate");
            String transactionId = request.getParameter("vnp_TransactionNo");
            String totalPrice = request.getParameter("vnp_Amount");

            model.addAttribute("orderId", orderInfo);
            model.addAttribute("totalPrice", totalPrice);
            model.addAttribute("paymentTime", paymentTime);
            model.addAttribute("transactionId", transactionId);

            return paymentStatus == 1 ? "success" : "fail";
        }
}

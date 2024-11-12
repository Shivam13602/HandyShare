package com.g02.handyShare.Payment.Service;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class PaymentService {

    private static final String STRIPE_SECRET_KEY = "sk_test_51QCMlpCj4cfMdtSgvGv3Y949jGNTGjZbSWHD6GKUJ0hczCCQ8f5SPtX91LqFA4RLPn96F4KsoG8mwjwPEmwEafUf00wIvarU3p"; 

  public PaymentService() {
      Stripe.apiKey = STRIPE_SECRET_KEY; 
  }


public Map<String, Object> createCheckoutSession(Long amount, String currency) {
  Map<String, Object> responseData = new HashMap<>();
  try {
      SessionCreateParams params = SessionCreateParams.builder()
              .setMode(SessionCreateParams.Mode.PAYMENT)
              .setSuccessUrl("http://localhost:3000/feedback")
              // .setCancelUrl("http://localhost:3000/cancel") //In case want to add a cancel endpoint
              .addLineItem(SessionCreateParams.LineItem.builder()
                      .setQuantity(1L)
                      .setPriceData(SessionCreateParams.LineItem.PriceData.builder()
                              .setCurrency(currency)
                              .setUnitAmount(amount)
                              .setProductData(SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                      .setName("Sample Product")
                                      .build())
                              .build())
                      .build())
              .build();

      Session session = Session.create(params);
      responseData.put("url", session.getUrl());
  } catch (StripeException e) {
      e.printStackTrace();
      responseData.put("error", "Failed to create checkout session: " + e.getMessage());
  }
  return responseData;
}
}
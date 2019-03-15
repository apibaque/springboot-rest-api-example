package cl.redbanc.payment.api.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import cl.redbanc.payment.api.model.PaymentDTO;
import cl.redbanc.payment.service.PaymentService;

/**
 *
 * A sample greetings controller to return greeting text.
 */
@RestController
public class PaymentController {
	
	@Autowired
	PaymentService paymentService;
	
	private final static Logger logger = LoggerFactory.getLogger(PaymentController.class);
	
	
	/**
	 *
	 * @param name
	 *            the name to greet
	 * @return greeting text
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/payments/consent")
	public ResponseEntity<PaymentDTO> addUser(@RequestBody PaymentDTO payment) {
		logger.info("Request body (ADD consent) from API: " + payment.toString());
		PaymentDTO response = paymentService.addPayment(payment);
		logger.info("Response body (ADD consent) from API: " + response.toString());
		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}
	
	@RequestMapping(method=RequestMethod.POST, value="/payments")
	public ResponseEntity<PaymentDTO> update(@RequestBody PaymentDTO payment) {
		logger.info("Request body (ADD payment) from API: " + payment.toString());
		PaymentDTO response = paymentService.updatePayment(payment);
		logger.info("Response body (ADD payment) from API: " + response.toString());
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/payments/{id}")
	public ResponseEntity<PaymentDTO> findById(@PathVariable String id) {
		logger.info("Request body (GET payment) from API idOrder: " + id);
		PaymentDTO response = paymentService.getPayment(id);
		logger.info("Response body (GET payment) from API idOrder: " + id + " data: " + response.toString());
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
}

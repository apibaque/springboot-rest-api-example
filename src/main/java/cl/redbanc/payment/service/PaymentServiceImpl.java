package cl.redbanc.payment.service;

import java.util.Date;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;

import javax.persistence.EntityNotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import cl.redbanc.payment.api.controllers.PaymentController;
import cl.redbanc.payment.api.model.PaymentDTO;
import cl.redbanc.payment.model.CreditorAccount;
import cl.redbanc.payment.model.DebtorAccount;
import cl.redbanc.payment.model.InstructedAmount;
import cl.redbanc.payment.model.Payment;
import cl.redbanc.payment.repository.PaymentRepository;

@Service
public class PaymentServiceImpl implements PaymentService {

	@Autowired
	private PaymentRepository paymentRepository;
	
	private final static Logger logger = LoggerFactory.getLogger(PaymentServiceImpl.class);

	private Payment findOne(String id) {
		Optional<Payment> entity = paymentRepository.findById(id);
		if (Objects.isNull(entity)) {
			throw new EntityNotFoundException(id);
		}
		return entity.get();
	}
	
	@Override
	public PaymentDTO getPayment(String id) {
		 try {
			 Payment entity = findOne(id);				
			 return new PaymentDTO().build(entity);
		 }catch(Exception e) {
			logger.error("Response body (GET payment) from API idOrder: " + id +  " Payment Not Found ");
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Payment Not Found", e);
		 }
		
	}

	@Override
	public PaymentDTO addPayment(PaymentDTO payment) {
		CreditorAccount creditorAccount = new CreditorAccount(payment.getCreditorAccount().getIdentification(), payment.getCreditorAccount().getName(), payment.getCreditorAccount().getDestinationDNI(), payment.getCreditorAccount().getSecondaryIdentificator());
		DebtorAccount debtorAccount = new DebtorAccount(payment.getDebtorAccount().getIdentification(), payment.getDebtorAccount().getName(), payment.getDebtorAccount().getDestinationDNI(), payment.getDebtorAccount().getSecondaryIdentificator());
		InstructedAmount instructedAmount = new InstructedAmount(payment.getInstructedAmount().getAmount(), payment.getInstructedAmount().getCurrency());
		Payment entity = new Payment(payment.getId().toString(),payment.getStatus().toString(), 
				payment.getCreationDate(), new Date(),
				debtorAccount, creditorAccount,instructedAmount);
		System.out.println("addPayment: " + entity.toString());
		
		paymentRepository.save(entity);
		
		return new PaymentDTO().build(entity);
	}

	@Override
	public PaymentDTO updatePayment(PaymentDTO payment) {

		Payment entity = this.findOne(payment.getId());
		entity.setStatus(payment.getStatus().toString());
		entity.setModificationDate(new Date());
		entity.setTransactionId(payment.getTransactionId());
		//entity.setTransactionId(generateRandom().toString());
		
		paymentRepository.save(entity);
		
		return new PaymentDTO().build(entity);
	}

	@Override
	public void deletePayment(String id) {
		paymentRepository.deleteById(id);
	}
	
	public Integer generateRandom() {
		Random rand = new Random(); 
		int rand_int1 = rand.nextInt(100000000); 
		return rand_int1;
		
	}

}

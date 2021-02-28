package com.tba.application.subscriber;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.tba.application.constants.YardModuleQueueConstants;
import com.tba.application.domain.MoveCraneRequest;
import com.tba.application.domain.PublisherEvent;
import com.tba.application.servive.CraneManagerService;

@Component
public class YardModuleSubscriber {
	
	@Autowired
	private CraneManagerService craneManagerService;
	
	@RabbitListener(queues = YardModuleQueueConstants.QUEUE)
	public void evaluateYardModuleMessage(PublisherEvent event) {
		
		System.out.println("evaluateYardModuleMessage :: event: " + event);
		
		MoveCraneRequest requestFromEvent = new MoveCraneRequest(event.getCraneId(), 
				event.getStartPosition(), event.getEndPosition());
		
		craneManagerService.evaluateCraneMovementFromEvent(requestFromEvent, event.getCanProcessDirectly());
		
	}

}

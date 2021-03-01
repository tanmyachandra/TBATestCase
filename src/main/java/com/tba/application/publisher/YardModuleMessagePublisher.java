package com.tba.application.publisher;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tba.application.constants.YardModuleQueueConstants;
import com.tba.application.domain.MoveCraneRequest;
import com.tba.application.domain.PublisherEvent;
import com.tba.application.entities.YardMapping;

@Component
public class YardModuleMessagePublisher {
	
	@Autowired
    private RabbitTemplate template;
	
	public void addMoveRequestToQueue(MoveCraneRequest request, 
			YardMapping yardMappingDetails, Boolean canDirectlyBeProcessed, Boolean isBeingParked) {

		PublisherEvent event = new PublisherEvent(yardMappingDetails.getYardModuleId(), request.getCraneId(), 
				request.getStartPosition(), request.getEndPosition(), canDirectlyBeProcessed, isBeingParked);
		
		System.out.println("addMoveRequestToQueue :: event: " + event);
		
		template.convertAndSend(YardModuleQueueConstants.EXCHANGE, YardModuleQueueConstants.ROUTING_KEY, event);
		
	}

}

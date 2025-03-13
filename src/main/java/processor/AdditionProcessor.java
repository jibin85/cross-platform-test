package processor;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;
import model.AdditionRequest;
import model.AdditionResponse;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;

import java.util.logging.Logger;

@ApplicationScoped
@Named("additionProcessor")
public class AdditionProcessor implements Processor {

    private static final Logger logger = Logger.getLogger(AdditionProcessor.class.getSimpleName());

    @Override
    public void process(Exchange exchange) throws Exception {
        AdditionRequest request = exchange.getIn().getBody(AdditionRequest.class);
        logger.info("The First and Second Numbers are : " +request.getNum1() +", " +request.getNum2());
        AdditionResponse response = add(request);
        exchange.getMessage().setBody(response);
        logger.info("The Sum is : " +response.getSum());
    }

    public AdditionResponse add(AdditionRequest request) {
        AdditionResponse response = new AdditionResponse();
        response.setSum(request.getNum1() + request.getNum2());
        return response;
    }
}

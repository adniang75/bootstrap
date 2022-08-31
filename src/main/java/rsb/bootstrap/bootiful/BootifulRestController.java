package rsb.bootstrap.bootiful;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import rsb.bootstrap.Customer;
import rsb.bootstrap.CustomerService;

import java.util.Collection;

@RequiredArgsConstructor
@RestController
public class BootifulRestController {

    private final CustomerService customerService;

    @GetMapping("/customers")
    Collection<Customer> get() {
        return this.customerService.findAll();
    }

}

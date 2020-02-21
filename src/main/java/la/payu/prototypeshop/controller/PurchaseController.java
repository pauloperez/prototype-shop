package la.payu.prototypeshop.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.payu.sdk.exceptions.ConnectionException;
import com.payu.sdk.exceptions.InvalidParametersException;
import com.payu.sdk.exceptions.PayUException;
import la.payu.prototypeshop.exception.NotEnoughProductsInStockException;
import la.payu.prototypeshop.model.Order;
import la.payu.prototypeshop.model.User;
import la.payu.prototypeshop.service.ShoppingCartService;
import la.payu.prototypeshop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.security.Principal;
import java.util.Optional;

@Controller
public class PurchaseController {

	@Autowired
	private UserService userService;

	@Autowired
	private ShoppingCartService shoppingCartService;

	@GetMapping("/purchase")
	public ModelAndView purchase() {

		ModelAndView modelAndView = new ModelAndView("/purchase");
		modelAndView.addObject("order", shoppingCartService.getPurchasingOrder());
		return modelAndView;
	}

	@PostMapping("/checkout")
	public ModelAndView checkout(Principal principal, @Valid Order order, BindingResult bindingResult) {

		if (!bindingResult.hasErrors()) {
			try {
				Order purchasingOrder = shoppingCartService.getPurchasingOrder();
				purchasingOrder.setCreditCardNumber(order.getCreditCardNumber());
				purchasingOrder.setCreditCardExpirationDate(order.getCreditCardExpirationDate());
				purchasingOrder.setCreditCardSecurityCode(order.getCreditCardSecurityCode());
				purchasingOrder.setPaymentMethod(order.getPaymentMethod());
				purchasingOrder.setInstallments(order.getInstallments());

				Optional<User> userOptional = userService.findByUsername(principal.getName());

				shoppingCartService.checkout(userOptional.get(), purchasingOrder);

			} catch (NotEnoughProductsInStockException e) {
				return purchase().addObject("outOfStockMessage", e.getMessage());
			} catch (ConnectionException | InvalidParametersException | PayUException | JsonProcessingException e) {
				return purchase().addObject("payUMessages", e.getMessage());
			}

		} else {
			return new ModelAndView("/purchase");
		}

		return new ModelAndView("redirect:/status", "order", order.getId());
	}

	@GetMapping("/status")
	public ModelAndView status(@RequestParam("order") Optional<Long> optionalOrderId) {

		if (optionalOrderId.isPresent()) {

			ModelAndView modelAndView = new ModelAndView("/status");
			modelAndView.addObject("order", shoppingCartService.getPurchasedOrder(optionalOrderId.get()));
			return modelAndView;
		}
		return new ModelAndView("redirect:/error");
	}
}

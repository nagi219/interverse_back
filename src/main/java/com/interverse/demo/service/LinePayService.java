package com.interverse.demo.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.HmacAlgorithms;
import org.apache.commons.codec.digest.HmacUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.interverse.demo.dto.LinePayDTO;
import com.interverse.demo.dto.OrderDTO;
import com.interverse.demo.dto.OrderDetailDTO;
import com.interverse.demo.model.Product;
import com.interverse.demo.model.ProductRepository;
import com.interverse.demo.model.linepay.CheckoutPaymentRequestForm;
import com.interverse.demo.model.linepay.ProductForm;
import com.interverse.demo.model.linepay.ProductPackageForm;
import com.interverse.demo.model.linepay.RedirectUrls;
import com.interverse.demo.util.PostApiUtil;


@Service
public class LinePayService{
	
	@Autowired
	private PostApiUtil postApiUtil;
	@Autowired
	private ProductRepository pRepository;
	
	
	public static String encrypt(final String keys, final String data) {
        return toBase64String(HmacUtils.getInitializedMac(HmacAlgorithms.HMAC_SHA_256, keys.getBytes()).doFinal(data.getBytes()));
    }

    public static String toBase64String(byte[] bytes) {
        byte[] byteArray = Base64.encodeBase64(bytes);
        return new String(byteArray);
    }
	
	
	public JsonNode LinePayPost(OrderDTO dto) {
		
		CheckoutPaymentRequestForm form = new CheckoutPaymentRequestForm();
	
		form.setAmount(dto.getTotalAmount());
		form.setCurrency("TWD");
		form.setOrderId(dto.getId().toString());
		
		ProductPackageForm productPackageForm = new ProductPackageForm();
		
		productPackageForm.setId(dto.getId().toString());
		productPackageForm.setName("Interverse");
		productPackageForm.setAmount(dto.getTotalAmount());
		
		
		ArrayList<ProductForm> ProductFormList = new ArrayList<>();
		
		List<OrderDetailDTO> productForms = dto.getOrderDetails();
		
		for (OrderDetailDTO orderDetailDTO : productForms) {
			
			
			System.out.println("總金額"+dto.getTotalAmount());
			
			
			ProductForm productForm = new ProductForm();
			Optional<Product> byId = pRepository.findById(orderDetailDTO.getProductId());
			Product product = byId.get();
			productForm.setId(product.getId().toString());
			productForm.setName(product.getName());
			productForm.setImageUrl("");
			
			System.out.println("數量"+orderDetailDTO.getQuantity());
			System.out.println("金額"+orderDetailDTO.getPrice());
			productForm.setQuantity(orderDetailDTO.getQuantity());
			productForm.setPrice(orderDetailDTO.getPrice());
			
			ProductFormList.add(productForm);
		}
		
		
		
		productPackageForm.setProducts(ProductFormList);
		
		
		
		ArrayList<ProductPackageForm> ProductPackageFormList = new ArrayList<>();
		ProductPackageFormList.add(productPackageForm);
		form.setPackages(ProductPackageFormList);
		RedirectUrls redirectUrls = new RedirectUrls();
		redirectUrls.setAppPackageName("123");
		redirectUrls.setConfirmUrl("http://localhost:5173/order/userOrders");
		redirectUrls.setCancelUrl("");
		form.setRedirectUrls(redirectUrls);
		
		ObjectMapper mapper = new ObjectMapper();
		
		LinePayDTO linePayDTO = new LinePayDTO();
		
		String channelId = "2005966585";
		String channelSecret = "6ae54e020168ccb8f6af8f34e21e2efe";
		String requestUri = "/v3/payments/request";
		String requestHttpUri = "https://sandbox-api-pay.line.me/v3/payments/request";
		String nonce = UUID.randomUUID().toString();
		try {
			System.out.println("body=>" +mapper.writeValueAsString(form));
			System.out.println("nonce=>" + nonce);
			String signature = encrypt(channelSecret, channelSecret + requestUri + mapper.writeValueAsString(form) + nonce);
			System.out.println("signature=>"+ signature );
			
			linePayDTO.setChannelId(channelId);
			linePayDTO.setChannelSecret(channelSecret);
			linePayDTO.setRequestUri(requestUri);
			linePayDTO.setNonce(nonce);
			linePayDTO.setSignature(signature);
			linePayDTO.setBody(mapper.writeValueAsString(form));
			linePayDTO.setRequestHttpUri(requestHttpUri);

			
			
			 JsonNode sendPost = postApiUtil.sendPost(linePayDTO);
			 
			 System.out.println("sendPost" +sendPost);
			 
			 return sendPost;
			
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
			
	}
	
	
	
}

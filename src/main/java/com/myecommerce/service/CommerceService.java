package com.myecommerce.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.myecommerce.converter.Converter;
import com.myecommerce.dto.CommerceDTO;
import com.myecommerce.dto.ResponseBody;
import com.myecommerce.entity.Category;
import com.myecommerce.entity.Commerce;
import com.myecommerce.entity.CommerceStyle;
import com.myecommerce.enumerator.CommerceMessages;
import com.myecommerce.enumerator.Result;
import com.myecommerce.repository.CategoryRepository;
import com.myecommerce.repository.CommerceRepository;
import com.myecommerce.repository.CommerceStyleRepository;

@Service("commerceService")
public class CommerceService {

	@Autowired
	@Qualifier("commerceRepository")
	private CommerceRepository repository;

	@Autowired
	@Qualifier("categoryRepository")
	private CategoryRepository categoryRepository;
	
	@Autowired
	@Qualifier("categoryService")
	private CategoryService categoryService;
	
	@Autowired
	@Qualifier("commerceStyleRepository")
	private CommerceStyleRepository commerceStyleRepository;
	
	@Autowired
	@Qualifier("commerceStyleService")
	private CommerceStyleService commerceStyleService;

	@Autowired
	@Qualifier("converter")
	private Converter converter;

	public ResponseBody insert(CommerceDTO commerceDTO) {
		ResponseBody response = new ResponseBody("insertCommerce");
		Commerce commerce = converter.getCommerceFromDTO(commerceDTO);
		try {
			if(repository.findByPath(commerce.getPath()) == null) {
				commerceDTO.setId(repository.save(commerce).getId());
				commerceStyleRepository.save(commerceDTO.getStyle());
				response.setMsg(CommerceMessages.INSERT_OK);
				response.setResult(Result.OK);
				response.setData(commerceDTO);
			} else {
				response.setMsg(CommerceMessages.INSERT_ERR_PATH);
				response.setResult(Result.ERROR);
			}
		} catch (Exception e) {
			response.setMsg(CommerceMessages.INSERT_ERR);
			response.setResult(Result.ERROR);
		}
		return response;
	}

	public ResponseBody delete(long id) {
		ResponseBody response = new ResponseBody("deleteCommerce");
		try {
			Commerce commerce = repository.findById(id);
			if(commerce != null) {
				repository.delete(commerce);
				List<Category> categories = categoryRepository.findByIdCommerce(id);
				for(Category category : categories) {
					categoryService.delete(category.getId());
				}
				CommerceStyle commerceStyle = commerceStyleRepository.findByIdCommerce(commerce.getId());
				if(commerceStyle != null) {
					commerceStyleService.delete(commerceStyle.getId());
				}
				response.setMsg(CommerceMessages.DELETE_OK);
				response.setResult(Result.OK);
			} else {
				response.setMsg(CommerceMessages.ERR_NOT_EXISTS);
				response.setResult(Result.ERROR);
			}
		} catch (Exception e) {
			response.setMsg(CommerceMessages.DELETE_ERR);
			response.setResult(Result.ERROR);
		}
		return response;
	}

	public ResponseBody get(long id) {
		ResponseBody response = new ResponseBody("getCommerce");
		try {
			Commerce commerce = repository.findById(id);
			if(commerce != null) {
				CommerceStyle commerceStyle = commerceStyleRepository.findByIdCommerce(commerce.getId());
				response.setMsg(CommerceMessages.GET_OK);
				response.setResult(Result.OK);
				response.setData(new CommerceDTO(commerce, commerceStyle));
			} else {
				response.setMsg(CommerceMessages.ERR_NOT_EXISTS);
				response.setResult(Result.ERROR);
			}
		} catch (Exception e) {
			response.setMsg(CommerceMessages.GET_ERR);
			response.setResult(Result.ERROR);
		}
		return response;
	}
	
	public ResponseBody getByPath(String path) {
		ResponseBody response = new ResponseBody("getCommerceByPath");
		try {
			Commerce commerce = repository.findByPath(path);
			if(commerce != null) {
				CommerceStyle commerceStyle = commerceStyleRepository.findByIdCommerce(commerce.getId());
				response.setMsg(CommerceMessages.GET_OK);
				response.setResult(Result.OK);
				response.setData(new CommerceDTO(commerce, commerceStyle));
			} else {
				response.setMsg(CommerceMessages.ERR_NOT_EXISTS);
				response.setResult(Result.ERROR);
			}
		} catch (Exception e) {
			response.setMsg(CommerceMessages.GET_ERR);
			response.setResult(Result.ERROR);
		}
		return response;
	}

	public ResponseBody getByUser(long idUser) {
		ResponseBody response = new ResponseBody("getCommercesByUser");
		List<Commerce> commerces = repository.findByIdUser(idUser);
		List<CommerceDTO> commercesDTO = new ArrayList<>();
		for(Commerce commerce : commerces) {
			commercesDTO.add((CommerceDTO) this.get(commerce.getId()).getData());
		}
		if(!commercesDTO.isEmpty()) {
			response.setMsg(CommerceMessages.GET_MANY_OK);
			response.setResult(Result.OK);
			response.setData(commercesDTO);
		} else {
			response.setMsg(CommerceMessages.GET_MANY_ERR);
			response.setResult(Result.ERROR);
		}
		return response;
	}
}

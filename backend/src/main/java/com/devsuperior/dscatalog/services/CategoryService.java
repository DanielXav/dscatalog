package com.devsuperior.dscatalog.services;

import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devsuperior.dscatalog.dto.CategoryDTO;
import com.devsuperior.dscatalog.entities.Category;
import com.devsuperior.dscatalog.repositories.CategoryRepository;
import com.devsuperior.dscatalog.services.exceptions.ResourceNotFoundException;

@Service
public class CategoryService {

	@Autowired
	private CategoryRepository repository;

	@Transactional(readOnly = true)
	public List<CategoryDTO> findAll() {
		List<Category> list = repository.findAll();

		/*
		 * Esse bloco foi feito em apenas uma linha utilizando o map. 
		 * List<CategoryDTO> listDto = new ArrayList<>(); 
		 * for (Category x : list) { 
		 * listDto.add(new CategoryDTO(x));
		 *  }
		 */

		return list.stream().map(x -> new CategoryDTO(x)).toList();
	}

	@Transactional(readOnly = true)
	public CategoryDTO findById(Long id) {
		Category entity = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Entity not found"));
		
		return new CategoryDTO(entity);
	}

	@Transactional
	public CategoryDTO insert(CategoryDTO dto) {
		Category entity = new Category();
		entity.setName(dto.getName());
		entity = repository.save(entity);
		return new CategoryDTO(entity);
	}

	@Transactional
	public CategoryDTO update(Long id, CategoryDTO dto) {
		try {
			Category entity = repository.getOne(id);
			entity.setName(dto.getName());
			entity = repository.save(entity);
			return new CategoryDTO(entity);
		}
		catch (EntityNotFoundException e) {
			throw new ResourceNotFoundException("Id not found " + id);
		}
	}
}

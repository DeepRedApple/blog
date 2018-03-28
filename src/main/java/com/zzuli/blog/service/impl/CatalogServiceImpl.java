package com.zzuli.blog.service.impl;

import java.util.List;
import java.util.Optional;

import com.zzuli.blog.domain.Catalog;
import com.zzuli.blog.domain.User;
import com.zzuli.blog.repository.CatalogRepository;
import com.zzuli.blog.service.CatalogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CatalogServiceImpl implements CatalogService {
	@Autowired
	private CatalogRepository catalogRepository;
	
	@Override
	public Catalog saveCatalog(Catalog catalog) {
		 // 判断重复
        List<Catalog> list = catalogRepository.findByUserAndName(catalog.getUser(),
        		catalog.getName());
        if(list !=null && list.size() > 0) {
            throw new IllegalArgumentException("该分类已经存在了");
        }
        return catalogRepository.save(catalog);
	}
	
	@Override
	public void removeCatalog(Long id) {
		catalogRepository.deleteById(id);
	}

	@Override
	public Optional<Catalog> getCatalogById(Long id) {
		return catalogRepository.findById(id);
	}

	@Override
	public List<Catalog> listCatalogs(User user) {
		return catalogRepository.findByUser(user);
	}

}

package com.profitableaccountingsystemapi.repo;

import com.profitableaccountingsystemapi.entity.Specification;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SpecificationRepository extends MongoRepository<Specification,String>
{

}

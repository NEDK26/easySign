package com.mila.easysign.module.contract.service;


import com.mila.easysign.module.contract.entity.SampleContract;
import org.springframework.lang.Contract;
import org.springframework.stereotype.Service;

@Service
public interface ContractService {


    byte[] getContract(SampleContract contract);
}

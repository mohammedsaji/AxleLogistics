package com.app.logistics.account.utils;

import com.app.logistics.account.dto.AccountRequest;
import com.app.logistics.account.dto.AccountResponse;
import com.app.logistics.account.entity.Account;
import com.app.logistics.common.utils.CommonMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AccountMapper extends CommonMapper<AccountRequest, AccountResponse, Account> {
}

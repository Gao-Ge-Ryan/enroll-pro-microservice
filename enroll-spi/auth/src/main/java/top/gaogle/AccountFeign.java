package top.gaogle;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "auth", contextId = "core")
public class AccountFeign {
}

package com.profitableaccountingsystemapi.controller;

import com.profitableaccountingsystemapi.common.APIResponse;
import com.profitableaccountingsystemapi.dto.*;
import com.profitableaccountingsystemapi.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@CrossOrigin
public class LoginController {

    @Autowired
    private LoginService loginService;

    @Autowired
    private GlobalPayloadDataDTO globalPayloadDataDTO;
    @PostMapping("/register")
    public ResponseEntity<APIResponse> signUp(@RequestBody SignUpRequestDTO signUpRequestDTO) {
        APIResponse apiResponse = loginService.signUp(signUpRequestDTO);

        return ResponseEntity
                .status(apiResponse.getStatus())
                .body(apiResponse);
    }
    @PostMapping("/login")
    public ResponseEntity<APIResponse> login(@RequestBody LoginRequestDTO loginRequestDTO) throws Exception {
        APIResponse apiResponse = loginService.login(loginRequestDTO);

        return ResponseEntity
                .status(apiResponse.getStatus())
                .body(apiResponse);
    }
    @GetMapping("/test")
    public ResponseEntity<APIResponse> test() {
        APIResponse apiResponse = new APIResponse();

        apiResponse.setData("This is Test Api");

        return ResponseEntity
                .status(apiResponse.getStatus())
                .body(apiResponse);
    }

    @GetMapping("/privateApi")
    public ResponseEntity<APIResponse> privateApi(@RequestHeader(value="authorization", defaultValue = "") String auth) throws Exception {
        APIResponse apiResponse = new APIResponse();

        apiResponse.setData("This is private Api");

        //System.out.println(globalPayloadDataDTO.getUserId());

        return ResponseEntity
                .status(apiResponse.getStatus())
                .body(apiResponse);
    }

    @PostMapping("/refreshToken")
    public ResponseEntity<APIResponse> refreshToken(@RequestBody TokenDTO tokenDTO) throws Exception {

        APIResponse apiResponse = loginService.refreshToken(tokenDTO);

        return ResponseEntity
                .status(apiResponse.getStatus())
                .body(apiResponse);
    }

    @PostMapping("/changePassword")
    public ResponseEntity<APIResponse> resetPasswordVerification(@RequestBody ResetTokenDTO resetTokenDTO) throws Exception {

        APIResponse apiResponse = loginService.resetTokenVerify(resetTokenDTO);

        return ResponseEntity
                .status(apiResponse.getStatus())
                .body(apiResponse);
    }

    @PutMapping("/changePassword")
    public ResponseEntity<APIResponse> updatePassword(@RequestBody UpdatePasswordDTO updatePasswordDTO) throws Exception {
        APIResponse apiResponse = loginService.updatePassword(updatePasswordDTO);

        return ResponseEntity
                .status(apiResponse.getStatus())
                .body(apiResponse);
    }

    @PostMapping("/forgetPassword")
    public ResponseEntity<APIResponse> forgetPassword(@RequestBody ForgetPasswordDTO forgetPasswordDTO) {

        APIResponse apiResponse = loginService.forgetPasswordService(forgetPasswordDTO);

        return ResponseEntity
                .status(apiResponse.getStatus())
                .body(apiResponse);
    }

}

package com.arkwith.starter.user;

import java.security.Principal;
import java.time.LocalDateTime;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping("/user")
public class UserController {

    private final UserService memberService;

    @GetMapping("/login")
    public String login() {
        return "pages/user/login";
    }

    @GetMapping("/signup")
    public String signup(MemberCreateForm memberCreateForm) {
        return "pages/user/signup";
    }

    @PostMapping("/signup")
    public String signup(@Valid MemberCreateForm memberCreateForm, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "pages/user/signup";
        }

        if (!memberCreateForm.getPassword1().equals(memberCreateForm.getPassword2())) {
            bindingResult.rejectValue("password2", "passwordInCorrect", "비밀번호가 일치하지 않습니다.");
            return "pages/user/signup";
        }
        try {
            memberService.create(memberCreateForm.getUsername(), memberCreateForm.getPassword1(),
                                 memberCreateForm.getNickname(), null, null, Role.USER,
                                 memberCreateForm.getEmail());
            log.info("회원가입 Username: {}, 사용자명: {}, 닉네임: {}, 이메일:{} ", 
            memberCreateForm.getUsername(), memberCreateForm.getName(), memberCreateForm.getNickname(), memberCreateForm.getEmail());                    
        } catch (DataIntegrityViolationException e) {
            e.printStackTrace();
            bindingResult.reject("signupFailed", "이미 등록된 사용자입니다.");
            return "pages/user/signup";
        } catch (Exception e) {
            e.printStackTrace();
            bindingResult.reject("signupFailed", e.getMessage());
            return "pages/user/signup";
        }
        return "redirect:login";
    }

    @PreAuthorize("isAuthenticated()") // 인증된 사용자만 접근 가능
    @GetMapping("/list")
    public String list(Model model, @RequestParam(value="page", defaultValue = "0") int page,
            @RequestParam(value="numPerPage", defaultValue = "10") int numPerPage,
            @RequestParam(value="kw", defaultValue = "") String kw) {
        // List<Question> questions = this.questionService.findAll();
        Page<Member> paging = this.memberService.getList(page, numPerPage, kw);
        model.addAttribute("paging", paging);
        model.addAttribute("kw", kw);
        return "pages/user/user_list";
    }

    @PreAuthorize("isAuthenticated()") // 인증된 사용자만 접근 가능
    @GetMapping("/create")
    public String create(MemberCreateForm memberCreateForm) {
        memberCreateForm.setMode("NEW");
        return "pages/user/user_form";
    }

    @PreAuthorize("isAuthenticated()") // 인증된 사용자만 접근 가능
    @PostMapping("/create")
    public String createUser(@Valid MemberCreateForm memberCreateForm, BindingResult bindingResult, Principal principal) {

        if (bindingResult.hasErrors()) {
            memberCreateForm.setMode("NEW");
            return "pages/user/user_form";
        }

        if (!memberCreateForm.getPassword1().equals(memberCreateForm.getPassword2())) {
            memberCreateForm.setMode("NEW");
            bindingResult.rejectValue("password2", "passwordInCorrect", "비밀번호가 일치하지 않습니다.");
            return "pages/user/user_form";
        }
        try {
            memberService.create(memberCreateForm.getUsername(), memberCreateForm.getPassword1(),
                                 memberCreateForm.getNickname(), memberCreateForm.getName(), memberCreateForm.getPicture(),
                                 memberCreateForm.getRole(), memberCreateForm.getEmail());
        } catch (DataIntegrityViolationException e) {
            e.printStackTrace();
            memberCreateForm.setMode("NEW");
            bindingResult.reject("userCreateFailed", "이미 등록된 사용자입니다.");
            return "pages/user/user_form";
        } catch (Exception e) {
            e.printStackTrace();
            memberCreateForm.setMode("NEW");
            bindingResult.reject("userCreateFailed", e.getMessage());
            return "pages/user/user_form";
        }
        return "redirect:/user/list";
    }

    @PreAuthorize("isAuthenticated()") // 인증된 사용자만 접근 가능
    @GetMapping("/update/{id}")
    public String updateForm(MemberCreateForm memberCreateForm, @PathVariable("id") Long id) {
        Member member = this.memberService.findById(id);

        memberCreateForm.setMode("UPDATE");
        memberCreateForm.setUsername(member.getUsername());
        memberCreateForm.setPassword1(member.getPassword());
        memberCreateForm.setPassword2(member.getPassword());
        memberCreateForm.setNickname(member.getNickname());
        memberCreateForm.setName(member.getName());
        memberCreateForm.setEmail(member.getEmail());
        memberCreateForm.setPicture(member.getPicture());
        memberCreateForm.setRole(member.getRole());
        memberCreateForm.setProvider(member.getProvider());
        memberCreateForm.setProviderId(member.getProviderId());
        return "pages/user/user_form";
    }

    @PreAuthorize("isAuthenticated()") // 인증된 사용자만 접근 가능
    @PostMapping("/update/{id}")
    public String update(@PathVariable("id") Long id, @Valid MemberCreateForm memberCreateForm, BindingResult bindingResult, Principal principal) {
        Member member = this.memberService.findById(id);

        if (bindingResult.hasErrors()) {
            memberCreateForm.setMode("UPDATE");
            return "pages/user/user_form";
        }

        member.setNickname(memberCreateForm.getNickname());
        member.setName(memberCreateForm.getName());
        member.setPicture(memberCreateForm.getPicture());
        member.setEmail(memberCreateForm.getEmail());
        member.setRole(memberCreateForm.getRole());
        member.setModifyDate(LocalDateTime.now());

        this.memberService.save(member);
        return "redirect:/user/update/" + id;
    }

    @PreAuthorize("isAuthenticated()") // 인증된 사용자만 접근 가능
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") Long id, Principal principal) {
        Member member = this.memberService.findById(id);
        this.memberService.delete(member);
        return "redirect:/user/list";
    }

    @PreAuthorize("isAuthenticated()") // 인증된 사용자만 접근 가능
    @GetMapping("/profile/{username}")
    public String updateProfileForm(MemberCreateForm memberCreateForm, @PathVariable("username") String username) {
        
        Member member = this.memberService.getUser(username);

        memberCreateForm.setUsername(member.getUsername());
        memberCreateForm.setPassword1(member.getPassword());
        memberCreateForm.setPassword2(member.getPassword());
        memberCreateForm.setNickname(member.getNickname());
        memberCreateForm.setName(member.getName());
        memberCreateForm.setEmail(member.getEmail());
        
        return "pages/user/user_profile";

    }

    @PreAuthorize("isAuthenticated()") // 인증된 사용자만 접근 가능
    @PostMapping("/profile/{username}")
    public String updateProfile(MemberCreateForm memberCreateForm, @PathVariable("username") String username, BindingResult bindingResult, Principal principal) {
        
        Member member = this.memberService.getUser(username);


        if (bindingResult.hasErrors()) {
            return "pages/user/user_profile";
        }

        member.setNickname(memberCreateForm.getNickname());
        member.setName(memberCreateForm.getName());
        member.setEmail(memberCreateForm.getEmail());
        member.setModifyDate(LocalDateTime.now());

        this.memberService.save(member);
        return "redirect:/user/profile/" + username;

    }

    @PreAuthorize("isAuthenticated()") // 인증된 사용자만 접근 가능
    @GetMapping("/password/{username}")
    public String changePasswordForm(MemberCreateForm memberCreateForm, @PathVariable("username") String username) {
        
        Member member = this.memberService.getUser(username);

        memberCreateForm.setUsername(member.getUsername());
        memberCreateForm.setPassword1(member.getPassword());
        memberCreateForm.setPassword2(member.getPassword());
        memberCreateForm.setNickname(member.getNickname());
        memberCreateForm.setName(member.getName());
        memberCreateForm.setEmail(member.getEmail());
        
        return "pages/user/change_password";

    }

    @PreAuthorize("isAuthenticated()") // 인증된 사용자만 접근 가능
    @PostMapping("/password/{username}")
    public String changePassword(MemberCreateForm memberCreateForm, @PathVariable("username") String username, BindingResult bindingResult, Principal principal) {
        
        Member member = this.memberService.getUser(username);

        if (member.getId() == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다.");
        }

        if (bindingResult.hasErrors()) {
            return "pages/user/change_password";
        }

        if (memberCreateForm.getPassword1().equals("")) {
            bindingResult.rejectValue("password1", "passwordInCorrect", "비밀번호는 공백일수 없습니다.");
            return "pages/user/change_password";
        }

        if (!memberCreateForm.getPassword1().equals(memberCreateForm.getPassword2())) {
            bindingResult.rejectValue("password2", "passwordInCorrect", "비밀번호가 일치하지 않습니다.");
            return "pages/user/change_password";
        }

        this.memberService.changePassword(member, memberCreateForm.getPassword1());
        return "redirect:/user/password/" + username;
    }
}

package com.arkwith.starter.user;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

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
                                 memberCreateForm.getNickname(), null, null,
                                 memberCreateForm.getEmail());
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

    @GetMapping("/create")
    public String create() {
        return "pages/user/user_form";
    }

    @GetMapping("/detail/{id}")
    public String detail(@PathVariable Long id, Model model, MemberUpdateForm memberUpdateForm) {
        Member member = this.memberService.findById(id);
        model.addAttribute("member", member);
        return "pages/user/user_detail";
    }

}

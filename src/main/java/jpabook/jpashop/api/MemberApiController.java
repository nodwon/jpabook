package jpabook.jpashop.api;

import jakarta.validation.Valid;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.service.MemberService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController // responseBody + controller
public class MemberApiController {
    private final MemberService memberService;

    @GetMapping("/api/v1/members")
    public List<Member> membersV1(){
        return memberService.findMembers();
        //entity member가 그래로 웹으로 노출된다.
    }
    @GetMapping("/api/v2/members")
    public Result membersV2() {
        List<Member> findMembers = memberService.findMembers();
        //엔티티 -> DTO 변환
        List<MemberDto> collect = findMembers.stream()
                .map(m -> new MemberDto(m.getUsername()))
                .collect(Collectors.toList());
        return new Result(collect);
    }
    @Data
    @AllArgsConstructor
    static class Result<T> {
        private T data;
    }
    @Data
    @AllArgsConstructor
    static class MemberDto {
        private String name;
    }

    @PostMapping("/api/v1/members")
    public CreateMemberResponse saveMemberV1(@RequestBody @Valid Member member){
        Long id = memberService.join(member);
        return  new CreateMemberResponse(id);
    }
    // 더 안전한 방법
    @PostMapping("/api/v2/members")
    public CreateMemberResponse saveMemberV2(@RequestBody @Valid CreateMemberRequest request){
        Member member = new Member();
        member.setUsername(request.getName());

        Long id =memberService.join(member);
        return  new CreateMemberResponse(id);
    }
    //API회원수정
    @PutMapping("/api/v2/members/{id}")
    public UpdateMemberResponse updateMemberV2(
            @PathVariable("id") Long id, //업데이트와 response와 request를 따로 만듬
            @RequestBody @Valid UpdateMemberRequest request){
        // 변경감지 형태로 짜라고함
        memberService.update(id, request.getName());
        // 별도의 쿼리를 짬
        Member findMember = memberService.findOne(id);
        return new UpdateMemberResponse(findMember.getId(), findMember.getUsername());
    }
    @Data
    static class UpdateMemberRequest{
        private String name;
    }
    @Data
    @AllArgsConstructor
    static class UpdateMemberResponse{
        private Long id;
        private String name;
    }
    @Data
    static class CreateMemberRequest{
        private String name;
    }
    @Data
    static class CreateMemberResponse{
        private  Long id;

        public CreateMemberResponse(Long id) {
            this.id = id;
        }
    }
}

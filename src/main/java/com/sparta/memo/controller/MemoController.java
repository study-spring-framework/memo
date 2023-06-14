package com.sparta.memo.controller;

import com.sparta.memo.dto.MemoRequestDto;
import com.sparta.memo.dto.MemoResponseDto;
import com.sparta.memo.entity.Memo;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class MemoController {

    // 임시 데이터베이스
    private final Map<Long,Memo> memoList = new HashMap<>();
    @PostMapping("/memos")
    public MemoResponseDto createMemo(@RequestBody MemoRequestDto requestDto) {
        // CREATE API

        // requestDto -> Entity
        Memo memo = new Memo(requestDto);

        // Memo Max Id Check
        Long maxId = memoList.size() > 0 ? Collections.max(memoList.keySet()) + 1 : 1;
        memo.setId(maxId);

        // DB 저장
        memoList.put(memo.getId(), memo);

        // Entity -> ResponseDto로 변환
        MemoResponseDto memoResponseDto = new MemoResponseDto(memo);

        return memoResponseDto;
    }

    @GetMapping("/memos")
    public List<MemoResponseDto> getMemos() {
        // Map To List
        List<MemoResponseDto> responseDtoList = memoList.values().stream()
                .map(MemoResponseDto::new).toList();

        return responseDtoList;
    }

    @PutMapping("/memos/{id}")
    public Long updateMemo(@PathVariable Long id, @RequestBody MemoRequestDto requestDto) {
        // 수정할 내용 즉, cotent - 클라이언트에서 Body 부분에서 넘어올 JSON 형식(@RequestBody를 써야하는걸 눈치채야함)

        // 해당 메모가 DB에 존재하는지 확인
        if(memoList.containsKey(id)){
            // 있으면 해당 메모 가져오기
            Memo memo = memoList.get(id);

            // 메모 수정
            memo.update(requestDto);
            return  memo.getId();
        } else {
            throw new IllegalArgumentException("선택한 메모는 존재하지 않습니다");
        }
    }

    @DeleteMapping("/memos/{id}")
    public Long deleteMemo(@PathVariable Long id){
        // 해당 메모가 DB에 존재하는지 확인
        if(memoList.containsKey(id)){
            // 해당 메모 삭제
            memoList.remove(id);
            return id;
        } else {
            throw new IllegalArgumentException("선택한 메모는 존재하지 않습니다");
        }
    }
}

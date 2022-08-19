package com.feedbackcontinuos.controller;

import com.feedbackcontinuos.dto.FeedbackCompletoDTO;
import com.feedbackcontinuos.dto.FeedbackCreateDTO;
import com.feedbackcontinuos.dto.FeedbackDTO;
import com.feedbackcontinuos.dto.PageDTO;
import com.feedbackcontinuos.exceptions.RegraDeNegocioException;
import com.feedbackcontinuos.service.FeedbackService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/feedback")
@RequiredArgsConstructor
@Validated
public class FeedbackController {

    private final FeedbackService feedbackService;

    @PostMapping
    public ResponseEntity<Void> create(@RequestBody @Valid FeedbackCreateDTO feedbackCreateDTO) throws RegraDeNegocioException {
        feedbackService.create(feedbackCreateDTO);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/gived")
    public ResponseEntity<PageDTO<FeedbackDTO>> getGivenFeedback(@RequestParam Integer page) throws RegraDeNegocioException{
        return ResponseEntity.ok(feedbackService.getGivedFeedbacks(page));
    }
    @GetMapping("/receveid")
    public ResponseEntity<PageDTO<FeedbackDTO>> getReceveidFeedback(@RequestParam Integer page) throws RegraDeNegocioException {
        return ResponseEntity.ok(feedbackService.getReceivedFeedbacks(page));
    }


    @GetMapping("/receveid-por-id")
    public ResponseEntity<PageDTO<FeedbackDTO>> getReceveIdFeedbackIdUser(@RequestParam Integer page, Integer id) throws RegraDeNegocioException {
        return ResponseEntity.ok(feedbackService.getReceivedFeedbacksIdUser(page, id));
    }

    @GetMapping("/gived-por-id")
    public ResponseEntity<PageDTO<FeedbackDTO>> getGivenFeedbackIdUser(@RequestParam Integer page, Integer id) throws RegraDeNegocioException{
        return ResponseEntity.ok(feedbackService.getGivedFeedbacksIdUser(page, id));
    }

}

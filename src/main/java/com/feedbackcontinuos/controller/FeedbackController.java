package com.feedbackcontinuos.controller;

import com.feedbackcontinuos.dto.FeedbackCompletoDTO;
import com.feedbackcontinuos.dto.FeedbackCreateDTO;
import com.feedbackcontinuos.exceptions.RegraDeNegocioException;
import com.feedbackcontinuos.service.FeedbackService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/feedback")
@RequiredArgsConstructor
public class FeedbackController {

    private final FeedbackService feedbackService;


    @PostMapping
    public ResponseEntity<Void> create(@RequestBody FeedbackCreateDTO feedbackCreateDTO) throws RegraDeNegocioException {
        feedbackService.create(feedbackCreateDTO);
        return ResponseEntity.ok().build();
    }


    @GetMapping("/receveid")
    public ResponseEntity<Page<FeedbackCompletoDTO>> getReceveidFeedback(@RequestParam Integer page) throws RegraDeNegocioException {
        return ResponseEntity.ok(feedbackService.getReceivedFeedbacks(page));
    }

    @GetMapping("/gived")
    public ResponseEntity<Page<FeedbackCompletoDTO>> getGivenFeedback(@RequestParam Integer page) throws RegraDeNegocioException{
        return ResponseEntity.ok(feedbackService.getGivedFeedbacks(page));
    }

}

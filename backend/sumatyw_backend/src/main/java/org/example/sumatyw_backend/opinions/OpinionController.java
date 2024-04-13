package org.example.sumatyw_backend.opinions;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping("/opinions")
public class OpinionController {

    private OpinionService opinionService;

    @PostMapping()
    public ResponseEntity addOpinion(@RequestBody OpinionInputDTO opinionInputDTO) {
        try {
            Opinion opinion = opinionService.addOpinion(OpinionDTOMapper.mapOpinionInputDTOToOpinion(opinionInputDTO));

            return new ResponseEntity<>(
                OpinionDTOMapper.mapOpinionToOpinionDTO(opinion),
                HttpStatus.OK
            );
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", e.getMessage()));
        }
    }

    @GetMapping()
    public ResponseEntity<List<OpinionDTO>> getOpinions() {
        List<Opinion> opinions = opinionService.getOpinions();
        return new ResponseEntity<>(opinions.stream().map(OpinionDTOMapper::mapOpinionToOpinionDTO).toList(), HttpStatus.OK);
    }


    @PutMapping("/{id}")
    public ResponseEntity<OpinionDTO> updateOpinion(@PathVariable("id") UUID id, @RequestBody OpinionInputDTO opinionInputDTO) {
        Opinion opinion = opinionService.updateOpinionById(id, OpinionDTOMapper.mapOpinionInputDTOToOpinion(opinionInputDTO));

        return new ResponseEntity<>(
            OpinionDTOMapper.mapOpinionToOpinionDTO(opinion),
            HttpStatus.OK
        );
    }
}

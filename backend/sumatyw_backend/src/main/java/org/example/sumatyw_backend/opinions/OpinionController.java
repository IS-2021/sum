package org.example.sumatyw_backend.opinions;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping("/opinions")
public class OpinionController {

    private OpinionService opinionService;

    @PostMapping()
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<OpinionDTO> addOpinion(@RequestBody @Valid OpinionInputDTO opinionInputDTO) {
            Opinion opinion = opinionService.addOpinion(OpinionDTOMapper.mapOpinionInputDTOToOpinion(opinionInputDTO));

            return new ResponseEntity<>(
                OpinionDTOMapper.mapOpinionToOpinionDTO(opinion),
                HttpStatus.OK
            );
    }

    @GetMapping()
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<OpinionDTO>> getOpinions() {
        List<Opinion> opinions = opinionService.getOpinions();
        return new ResponseEntity<>(opinions.stream().map(OpinionDTOMapper::mapOpinionToOpinionDTO).toList(), HttpStatus.OK);
    }


    @PutMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<OpinionDTO> updateOpinion(@PathVariable("id") UUID id, @RequestBody @Valid OpinionInputDTO opinionInputDTO) {
        Opinion opinion = opinionService.updateOpinionById(id, OpinionDTOMapper.mapOpinionInputDTOToOpinion(opinionInputDTO));

        return new ResponseEntity<>(
            OpinionDTOMapper.mapOpinionToOpinionDTO(opinion),
            HttpStatus.OK
        );
    }
}

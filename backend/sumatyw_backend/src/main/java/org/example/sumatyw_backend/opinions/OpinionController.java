package org.example.sumatyw_backend.opinions;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping("/opinions")
public class OpinionController {

    private OpinionService opinionService;

    @PostMapping()
    public ResponseEntity<OpinionDTO> addOpinion(@RequestBody @Valid OpinionInputDTO opinionInputDTO) {
            Opinion opinion = opinionService.addOpinion(OpinionDTOMapper.mapOpinionInputDTOToOpinion(opinionInputDTO));

            return new ResponseEntity<>(
                OpinionDTOMapper.mapOpinionToOpinionDTO(opinion),
                HttpStatus.OK
            );
    }

    @GetMapping()
    public ResponseEntity<OpinionDTO> getOpinionById(@RequestParam("userId") UUID userId,
                                                        @RequestParam("restaurantId") UUID restaurantId) {

        return new ResponseEntity<>(OpinionDTOMapper.mapOpinionToOpinionDTO(opinionService.getOpinionByUserIdRestaurantId(userId,restaurantId)), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<OpinionDTO> updateOpinion(@PathVariable("id") UUID id, @RequestBody @Valid OpinionInputDTO opinionInputDTO) {
        Opinion opinion = opinionService.updateOpinionById(id, OpinionDTOMapper.mapOpinionInputDTOToOpinion(opinionInputDTO));

        return new ResponseEntity<>(
            OpinionDTOMapper.mapOpinionToOpinionDTO(opinion),
            HttpStatus.OK
        );
    }

    @DeleteMapping()
    public ResponseEntity<OpinionDTO> deleteOpinionById(@RequestParam("opinionId") UUID opinionId) {

        return new ResponseEntity<>(OpinionDTOMapper.mapOpinionToOpinionDTO(opinionService.deleteOpinion(opinionId)),HttpStatus.OK);
    }
}

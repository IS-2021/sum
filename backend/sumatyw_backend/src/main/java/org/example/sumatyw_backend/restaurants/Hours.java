package org.example.sumatyw_backend.restaurants;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Hours {

    public List<String> monday;
    public List<String> tuesday;
    public List<String> wednesday;
    public List<String> thursday;
    public List<String> friday;
    public List<String> saturday;
    public List<String> sunday;


}

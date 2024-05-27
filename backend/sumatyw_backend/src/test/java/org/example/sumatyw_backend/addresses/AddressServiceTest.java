package org.example.sumatyw_backend.addresses;

import com.google.maps.errors.ApiException;
import com.google.maps.model.PlaceDetails;
import org.example.sumatyw_backend.geo.place_details.PlaceDetailsMapper;
import org.example.sumatyw_backend.geo.place_details.PlaceDetailsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.parameters.P;

import java.io.IOException;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AddressServiceTest {

    private AddressService addressService;
    @Mock
    private AddressRepository addressRepository;
    @Mock
    private PlaceDetailsService placeDetailsService;

    @BeforeEach
    void setUp() {
        addressService = new AddressService(addressRepository, placeDetailsService);
    }

    @Test
    void getAddress_ReturnsAddress_IfExistsInDatabase() throws IOException, InterruptedException, ApiException {
        // given
        String addressId = "adsfjasifqewfnqeaf";
        Address address = Address.builder().addressId(addressId).build();

        given(addressRepository.findById(address.getAddressId())).willReturn(Optional.of(address));

        // when
        Address foundAddress = addressService.getAddress(addressId);

        // then
        ArgumentCaptor<String> addressIdCaptor = ArgumentCaptor.forClass(String.class);

        verify(addressRepository).findById(addressIdCaptor.capture());
        verify(placeDetailsService, never()).getPlaceDetails(any());
        verify(addressRepository, never()).save(any());

        String capturedAddressId = addressIdCaptor.getValue();

        assertThat(capturedAddressId).isEqualTo(addressId);
        assertThat(foundAddress).isEqualTo(address);
    }

//    @Test
//    void getAddress_GetsAddressFromAPIAndReturnsIt_IfDoesNotExistInDatabase() throws IOException, InterruptedException, ApiException {
//        // given
//        String addressId = "adsfjasifqewfnqeaf";
//        Address address = Address.builder().addressId(addressId).build();
//        PlaceDetails placeDetails = new PlaceDetails();
//        placeDetails.adrAddress = addressId;
//
//        given(addressRepository.findById(address.getAddressId())).willReturn(Optional.empty());
//        given(placeDetailsService.getPlaceDetails(addressId)).willReturn(placeDetails);
//
//        // when
//        Address foundAddress = addressService.getAddress(addressId);
//
//        // then
//        ArgumentCaptor<String> addressIdCaptor1 = ArgumentCaptor.forClass(String.class);
//        ArgumentCaptor<String> addressIdCaptor2 = ArgumentCaptor.forClass(String.class);
//        ArgumentCaptor<PlaceDetails> placeDetailsCaptor = ArgumentCaptor.forClass(PlaceDetails.class);
//        ArgumentCaptor<Address> addressCaptor = ArgumentCaptor.forClass(Address.class);
//
//        verify(addressRepository).findById(addressIdCaptor1.capture());
//        verify(placeDetailsService).getPlaceDetails(addressIdCaptor2.capture());
//        try (MockedStatic<PlaceDetailsMapper> placeDetailsMapper = mockStatic(PlaceDetailsMapper.class)) {
//            placeDetailsMapper.when(() -> PlaceDetailsMapper.mapPlaceDetailsToAddress(placeDetailsCaptor.capture())).thenReturn(address);
//        }
//        verify(addressRepository).save(addressCaptor.capture());
//
//        String capturedAddressId1 = addressIdCaptor1.getValue();
//        String capturedAddressId2 = addressIdCaptor2.getValue();
//        PlaceDetails capturedPlaceDetails = placeDetailsCaptor.getValue();
//        Address capturedAddress = addressCaptor.getValue();
//
//        assertThat(capturedAddressId1).isEqualTo(addressId);
//        assertThat(capturedAddressId2).isEqualTo(addressId);
//        assertThat(capturedPlaceDetails).isEqualTo(placeDetails);
//        assertThat(capturedAddress).isEqualTo(address);
//        assertThat(foundAddress).isEqualTo(address);
//    }
}

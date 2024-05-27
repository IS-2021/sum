import { Loader } from '@googlemaps/js-api-loader';
import type { AddressDTO } from '@/lib/api-model';

export const loader = new Loader({
  apiKey: import.meta.env.VITE_GOOGLE_MAPS_API_KEY,
  version: 'weekly',
});

export type Coordinates = {
  latitude: number;
  longitude: number;
};

export function formatAddress(address: AddressDTO) {
  return `${address.street} ${address.number}, ${address.city}, ${address.country}`;
}

export function formatAddressShort(address: AddressDTO) {
  return `${address.street} ${address.number}`;
}

export type GoogleMapsMarkerClickEvent = {
  latLng: google.maps.LatLng;
  domEvent: MouseEvent;
};

import { Loader } from '@googlemaps/js-api-loader';

const loader = new Loader({
  apiKey: import.meta.env.VITE_GOOGLE_MAPS_API_KEY as string,
  version: 'weekly',
  libraries: ['places'],
});

export const placesApi = await loader.importLibrary('places');

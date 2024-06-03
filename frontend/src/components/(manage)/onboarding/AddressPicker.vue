<script setup lang="ts">
import { formatAddress } from '@/lib/googleMaps';
import { Button } from '@/components/ui/button';
import { LocateFixedIcon, MapPinIcon } from 'lucide-vue-next';
import { Alert, AlertDescription, AlertTitle } from '@/components/ui/alert';
import GoogleMaps from '@/components/maps/GoogleMaps.vue';
import AddressAutocompleteInput from '@/components/maps/autocomplete/AddressAutocompleteInput.vue';
import type { AddressDTO } from '@/lib/api-model';
import { useAddressSettings } from '@/components/maps/useAddressSettings';
import { watchEffect } from 'vue';

const emit = defineEmits<{
  (e: 'save:address', payload: AddressDTO): void;
}>();

const { coords, handleMapsClick, address, isGeolocationSupported, setPlaceId, resumeGeolocation } =
  useAddressSettings({});

watchEffect(() => {
  if (address.value) {
    emit('save:address', address.value);
  }
});
</script>

<template>
  <div class="flex flex-col gap-2 sm:flex-row">
    <AddressAutocompleteInput
      popover-class="md:w-80 lg:w-full lg:max-w-prose"
      @on-place-select="setPlaceId"
    />

    <Button
      v-if="isGeolocationSupported"
      @click="resumeGeolocation"
      variant="outline"
      type="button"
    >
      <LocateFixedIcon class="mr-2 h-4 w-4" /> Use my current location
    </Button>
  </div>

  <GoogleMaps
    class="mt-3 h-[400px] border border-neutral-300"
    :zoom="12"
    :pan-zoom="15"
    :latitude="coords.latitude"
    :longitude="coords.longitude"
    :onClick="handleMapsClick"
  />

  <Alert class="mt-3 border-none" v-if="address && address.addressId">
    <MapPinIcon class="h-4 w-4" />
    <AlertTitle>Picked address</AlertTitle>
    <AlertDescription>
      {{ formatAddress(address) }}
    </AlertDescription>
  </Alert>
</template>

<script setup lang="ts">
import AddressAutocompleteInput from '@/components/maps/autocomplete/AddressAutocompleteInput.vue';
import { LocateFixedIcon, MapPinIcon } from 'lucide-vue-next';
import { formatAddress } from '@/lib/googleMaps';
import { Alert, AlertDescription, AlertTitle } from '@/components/ui/alert';
import GoogleMaps from '@/components/maps/GoogleMaps.vue';
import { useAddressSettings } from '@/components/maps/useAddressSettings';
import type { AddressDTO } from '@/lib/api-model';
import { useRefOverrideWithStatus } from '@/composables/maps/useRefOverride';
import { toRef, watchEffect } from 'vue';
import { Button } from '@/components/ui/button';

const props = defineProps<{
  address: AddressDTO;
}>();

const emit = defineEmits<{
  (e: 'save:address', payload: AddressDTO): void;
}>();

const {
  coords,
  handleMapsClick,
  address: settingsAddress,
  isGeolocationSupported,
  setPlaceId,
  resumeGeolocation,
} = useAddressSettings({});

const { value: address, isOverridden } = useRefOverrideWithStatus(
  toRef(props.address),
  settingsAddress,
);

watchEffect(() => {
  if (address.value && isOverridden.value) {
    emit('save:address', address.value);
  }
});
</script>

<template>
  <Alert class="mb-3 border-none">
    <MapPinIcon class="h-4 w-4" />
    <AlertTitle>Restaurant address</AlertTitle>
    <AlertDescription>
      {{ formatAddress(address) }}
    </AlertDescription>
  </Alert>

  <div class="flex gap-2">
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
    :zoom="16"
    :pan-zoom="16"
    :latitude="coords.latitude"
    :longitude="coords.longitude"
    :onClick="handleMapsClick"
  />
</template>

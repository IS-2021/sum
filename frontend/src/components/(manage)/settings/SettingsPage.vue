<script setup lang="ts">
import {
  ImageField,
  RestaurantDetailsFields,
  RestaurantHoursFields,
} from '@/components/(manage)/common/fields';
import { toTypedSchema } from '@vee-validate/zod';
import {
  mapDTOToRestaurantData,
  mapRestaurantDataToDTO,
  restaurantSchema,
} from '@/components/(manage)/onboarding/restaurantSchema';
import { useForm } from 'vee-validate';
import type { RestaurantDTO } from '@/lib/api-model';
import HoursFormTip from '@/components/(manage)/common/HoursFormTip.vue';
import AddressAutocompleteInput from '@/components/maps/autocomplete/AddressAutocompleteInput.vue';
import { useAddress } from '@/composables/maps/useAddress';
import { Separator } from '@/components/ui/separator';
import SettingsSection from '@/components/(manage)/settings/SettingsSection.vue';
import { Button } from '@/components/ui/button';
import { MapPinIcon, SaveIcon } from 'lucide-vue-next';
import { computed, watchEffect } from 'vue';
import { putRestaurantsId } from '@/lib/api/restaurants/restaurants';
import { toast } from 'vue-sonner';
import { formatAddress } from '@/lib/googleMaps';
import { Alert, AlertDescription, AlertTitle } from '@/components/ui/alert';
import { useImage } from '@/components/(manage)/common/image/useImage';
import ImagePreview from '@/components/(manage)/common/image/ImagePreview.vue';
import { getImageUrl } from '@/lib/assets';

const { restaurant } = defineProps<{ restaurant: RestaurantDTO }>();

const formSchema = toTypedSchema(restaurantSchema);
const form = useForm({
  validationSchema: formSchema,
  initialValues: mapDTOToRestaurantData(restaurant),
});

const { address, setPlaceId } = useAddress();
const { image, setImage } = useImage();

const imageUrl = computed(() => {
  if (image.value.previewUrl !== '') {
    return image.value.previewUrl;
  } else {
    return getImageUrl(restaurant.imageUrl);
  }
});

watchEffect(() => {
  if (address.value) {
    form.setValues({
      ...form.values,
      address: address.value,
    });
  }
});

const onSubmit = form.handleSubmit(async (values) => {
  const updateData = mapRestaurantDataToDTO(values);

  const res = await putRestaurantsId(restaurant.id, updateData);

  if (res.status === 200) {
    form.resetForm({
      values: mapDTOToRestaurantData(res.data),
    });
  } else {
    console.error('Failed to update restaurant', res);
    toast.error('Failed to update restaurant');
  }
});
</script>

<template>
  <form @submit="onSubmit">
    <header class="sticky top-0 flex max-w-screen-md justify-between">
      <h1 class="mb-8 text-2xl font-semibold tracking-tight">Settings</h1>

      <Button type="submit" v-if="form.meta.value.dirty" :disabled="!form.meta.value.valid">
        <SaveIcon class="mr-2 h-4 w-4" /> Save
      </Button>
    </header>

    <SettingsSection>
      <h2 class="text-lg font-semibold tracking-tight">Restaurant profile</h2>
      <Separator class="mb-4 mt-2" />

      <div class="mb-4 mt-4 space-y-4">
        <RestaurantDetailsFields />
      </div>
      <p class="mb-2 text-sm font-medium">Restaurant photo</p>
      <ImageField @on-change="setImage" />
      <ImagePreview v-if="imageUrl" :src="imageUrl" />
    </SettingsSection>

    <SettingsSection>
      <h2 class="text-lg font-semibold tracking-tight">Opening Hours</h2>
      <Separator class="mb-4 mt-2" />

      <RestaurantHoursFields />
      <HoursFormTip class="mt-4" />
    </SettingsSection>

    <SettingsSection>
      <h2 class="mb-2 text-lg font-semibold tracking-tight">Location</h2>
      <Separator class="mb-4 mt-2" />

      <Alert class="mb-3">
        <MapPinIcon class="h-4 w-4" />
        <AlertTitle>Current address</AlertTitle>
        <AlertDescription>
          {{ formatAddress(restaurant.address) }}
        </AlertDescription>
      </Alert>
      <AddressAutocompleteInput
        popover-class="md:w-80 lg:w-full lg:max-w-prose"
        @on-place-select="setPlaceId"
      />
    </SettingsSection>
  </form>

  <SettingsSection>
    <h2 class="mb-2 text-lg font-semibold tracking-tight text-red-700">Danger zone</h2>
    <Separator class="mb-4 mt-2" />

    <Button variant="destructive">Deactivate restaurant</Button>
  </SettingsSection>
</template>

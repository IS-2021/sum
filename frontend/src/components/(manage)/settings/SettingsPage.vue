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
import { computed, toRef, watchEffect } from 'vue';
import { putRestaurantsDeactivateId, putRestaurantsId } from '@/lib/api/restaurants/restaurants';
import { toast } from 'vue-sonner';
import { formatAddress } from '@/lib/googleMaps';
import { Alert, AlertDescription, AlertTitle } from '@/components/ui/alert';
import { useImage } from '@/components/(manage)/common/image/useImage';
import ImagePreview from '@/components/(manage)/common/image/ImagePreview.vue';
import { getImageUrl } from '@/lib/assets';
import { uploadRestaurantImage } from '@/components/(manage)/common/image/api';
import { useRestaurantUser } from '@/composables/useRestaurantUser';
import { useRefOverride } from '@/composables/maps/useRefOverride';
import RestaurantStatus from '@/components/(manage)/common/RestaurantStatus.vue';
import ConfirmDialog from '@/components/dialogs/ConfirmDialog.vue';

const props = defineProps<{ initialRestaurantData: RestaurantDTO }>();

const { restaurant: restaurantUser, invalidateCache } = useRestaurantUser();
const restaurant = useRefOverride(toRef(props.initialRestaurantData), restaurantUser);

const formSchema = toTypedSchema(restaurantSchema);
const form = useForm({
  validationSchema: formSchema,
  initialValues: mapDTOToRestaurantData(props.initialRestaurantData),
});

const { address, setPlaceId } = useAddress();
const { image, setImage } = useImage();

const imageUrl = computed(() => {
  if (image.value.previewUrl !== '') {
    return image.value.previewUrl;
  } else {
    return getImageUrl(restaurant.value.imageUrl);
  }
});

const isFormDirty = computed(() => form.meta.value.dirty || image.value.file !== null);

watchEffect(() => {
  if (address.value) {
    form.setValues({
      ...form.values,
      address: address.value,
    });
  }
});

async function deactivateRestaurant() {
  const res = await putRestaurantsDeactivateId(restaurant.value.id);

  if (res.status === 200) {
    await invalidateCache();
    toast.info('Restaurant deactivated');
  } else {
    console.error('Failed to deactivate restaurant', res);
    toast.error('Failed to deactivate restaurant');
  }
}

const onSubmit = form.handleSubmit(async (values) => {
  const updateData = mapRestaurantDataToDTO(values);

  const updateRes = await putRestaurantsId(restaurant.value.id, updateData);
  if (updateRes.status === 200) {
    form.resetForm({
      values: mapDTOToRestaurantData(updateRes.data),
    });
    await invalidateCache();
  } else {
    console.error('Failed to update restaurant', updateRes);
    toast.error('Failed to update restaurant');
  }

  let imageUploadRes: Awaited<ReturnType<typeof uploadRestaurantImage>> | undefined;
  if (image.value.file) {
    imageUploadRes = await uploadRestaurantImage(restaurant.value.id, image.value.file);
    if (imageUploadRes.status === 200) {
      image.value = { file: null, previewUrl: '' };
      await invalidateCache();
    } else {
      console.error('Failed to upload image', imageUploadRes);
    }
  }
});
</script>

<template>
  <form @submit="onSubmit">
    <header class="flex max-w-screen-md justify-between">
      <h1 class="mb-8 text-2xl font-semibold tracking-tight">Settings</h1>

      <Button type="submit" v-if="isFormDirty" :disabled="!form.meta.value.valid">
        <SaveIcon class="mr-2 h-4 w-4" /> Save
      </Button>
    </header>

    <SettingsSection class="pb-8">
      <h2 class="text-lg font-semibold tracking-tight">Restaurant profile</h2>
      <Separator class="mb-4 mt-2" />

      <div class="mb-4 mt-4 space-y-4">
        <RestaurantStatus :status="restaurant.status" />

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

    <ConfirmDialog>
      <template v-slot:trigger>
        <Button variant="destructive" :disabled="restaurant.status !== 'Active'">
          Deactivate restaurant
        </Button>
      </template>
      <template v-slot:description>
        <p>
          Are you sure you want to deactivate your restaurant? It will have to be activated again by
          one of admins.
        </p>
      </template>
      <template v-slot:confirmButton>
        <Button variant="destructive" @click="deactivateRestaurant">Confirm</Button>
      </template>
    </ConfirmDialog>
  </SettingsSection>
</template>

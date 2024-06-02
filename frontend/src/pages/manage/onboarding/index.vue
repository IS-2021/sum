<script setup lang="ts">
import { useRestaurantUser } from '@/composables/useRestaurantUser';
import { useHead } from '@unhead/vue';
import { Button } from '@/components/ui/button';
import { ref, watchEffect } from 'vue';
import { toTypedSchema } from '@vee-validate/zod';
import {
  mapRestaurantDataToDTO,
  restaurantSchema,
  restaurantSchemaDefaults,
} from '@/components/(manage)/onboarding/restaurantSchema';
import { useForm } from 'vee-validate';
import { useStepper } from '@vueuse/core';
import { cn } from '@/lib/utils';
import { ArrowRightIcon, ChevronLeftIcon, ChevronRightIcon, LogOutIcon } from 'lucide-vue-next';
import Logo from '@/components/Logo.vue';
import { useAddress } from '@/composables/maps/useAddress';
import { postRestaurants } from '@/lib/api/restaurants/restaurants';
import { useRouter } from 'vue-router/auto';
import {
  ImageField,
  RestaurantDetailsFields,
  RestaurantHoursFields,
} from '@/components/(manage)/common/fields';
import HoursFormTip from '@/components/(manage)/common/HoursFormTip.vue';
import type { AddressDTO, ProblemDetailResponse } from '@/lib/api-model';
import type { ImageChangeEvent, ImageFieldData } from '@/components/(manage)/common/fields/types';
import { uploadRestaurantImage } from '@/components/(manage)/common/image/api';
import ImagePreview from '@/components/(manage)/common/image/ImagePreview.vue';
import AddressPicker from '@/components/(manage)/onboarding/AddressPicker.vue';

useHead({
  title: 'Complete restaurant profile',
});

const router = useRouter();
const { user, signOut, invalidateCache } = useRestaurantUser();
const { address, setPlaceId } = useAddress();
const errorMessage = ref('');

const { current, goToPrevious, goToNext, isCurrent, isFirst, isBefore, goTo } = useStepper({
  greeting: {
    label: 'Welcome to FoodGood',
    description:
      'Before you continue, you need to finish setting up your restaurant profile. After your restaurant profile is verified, you can start your food saving journey.',
  },
  details: {
    label: 'Restaurant details',
    description: 'First, we need some basic information about your restaurant.',
  },
  hours: {
    label: 'Opening hours',
    description: 'Next, let us know when your restaurant is open.',
  },
  location: {
    label: 'Restaurant location',
    description: 'Finally, tell us where your restaurant is located.',
  },
  photo: {
    label: 'Restaurant photo',
    description:
      'Upload a photo of your restaurant to make it stand out. This step is optional and photo can be changed later in the settings.',
  },
});

const formSchema = toTypedSchema(restaurantSchema);
const form = useForm({
  validationSchema: formSchema,
  initialValues: restaurantSchemaDefaults,
});

watchEffect(() => {
  if (address.value) {
    form.setValues({
      ...form.values,
      address: address.value,
    });
  }
});

watchEffect(() => {
  if (user.value) {
    form.setValues({
      ...form.values,
      ownerId: user.value.id,
    });
  }
});

const onSubmit = form.handleSubmit(async (formValues) => {
  const requestData = mapRestaurantDataToDTO(formValues);
  const res = await postRestaurants(requestData);

  if (res.status === 200) {
    errorMessage.value = '';

    await invalidateCache();
    goTo('photo');
  } else if (res.status === 400) {
    const { detail } = res.data as unknown as ProblemDetailResponse;

    if (detail) {
      errorMessage.value = detail;
    }
  }
});

const image = ref<ImageFieldData>({
  file: null,
  previewUrl: '',
});

function setImage({ file, previewUrl }: ImageChangeEvent) {
  image.value = {
    file,
    previewUrl,
  };
}

function setFormAddress(address: AddressDTO) {
  form.setValues({
    ...form.values,
    address,
  });
}

async function handleSaveRestaurantImage() {
  if (!user.value?.id || !image.value.file) {
    return;
  }

  const res = await uploadRestaurantImage(user.value.id, image.value.file);

  if (res.status === 200) {
    await invalidateCache();
    await router.push('/manage');
  }
}
</script>

<template>
  <div class="min-h-svh grid-cols-2 lg:grid">
    <div class="hidden border-r border-neutral-300 lg:block">
      <img
        src="@/assets/images/chris-liverani-oCsaxvGCehM-unsplash-crop.jpg"
        alt="FoodGood background image"
        class="aria-hidden h-svh w-full object-cover object-right-top opacity-70"
      />
    </div>

    <div class="p-10 md:h-svh">
      <div class="flex justify-between">
        <Logo class="mb-8" />

        <Button variant="ghost" class="text-neutral-500" @click="signOut">
          <LogOutIcon class="mr-2 h-4 w-4" /> Sign out
        </Button>
      </div>

      <h1 class="mb-2 text-2xl font-bold">{{ current.label }}</h1>

      <p class="mb-8 max-w-prose text-neutral-700">{{ current.description }}</p>

      <form @submit="onSubmit" v-if="isBefore('photo')">
        <!-- Details -->
        <div :class="cn('hidden space-y-4', isCurrent('details') && 'block')">
          <RestaurantDetailsFields />
        </div>

        <!-- Hours -->
        <div :class="cn('hidden', isCurrent('hours') && 'block')">
          <HoursFormTip />
          <RestaurantHoursFields />
        </div>

        <!-- Location -->
        <div :class="cn('hidden', isCurrent('location') && 'block')">
          <AddressPicker @save:address="setFormAddress" />
        </div>

        <div class="mt-8 flex gap-2">
          <Button type="button" :disabled="isFirst" variant="outline" @click="goToPrevious">
            <ChevronLeftIcon class="mr-2 h-4 w-4" /> Back
          </Button>
          <Button type="button" @click="goToNext" v-if="isBefore('location')">
            Continue <ChevronRightIcon class="ml-2 h-4 w-4" />
          </Button>
          <Button v-else type="submit" :disabled="!form.meta.value.valid && isCurrent('location')"
            >Save</Button
          >
        </div>
      </form>

      <div v-else-if="isCurrent('photo')">
        <ImageField @on-change="setImage" />

        <ImagePreview v-if="image.previewUrl" :src="image.previewUrl" />

        <div class="mt-6 flex gap-2">
          <Button @click="handleSaveRestaurantImage" :disabled="!image.file"> Save </Button>
          <Button variant="outline" as-child>
            <RouterLink to="/manage">
              Skip for now <ArrowRightIcon class="ml-2 h-4 w-4" />
            </RouterLink>
          </Button>
        </div>
      </div>
    </div>
  </div>
</template>

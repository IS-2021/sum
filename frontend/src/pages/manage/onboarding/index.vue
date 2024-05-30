<script setup lang="ts">
import { getRestaurantUserQueryKey, useRestaurantUser } from '@/composables/useRestaurantUser';
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
import { AlertCircleIcon, ChevronLeftIcon, ChevronRightIcon, LogOutIcon } from 'lucide-vue-next';
import Logo from '@/components/Logo.vue';
import AddressAutocompleteInput from '@/components/maps/autocomplete/AddressAutocompleteInput.vue';
import { useAddress } from '@/composables/maps/useAddress';
import { postRestaurants } from '@/lib/api/restaurants/restaurants';
import { useRouter } from 'vue-router/auto';
import { useQueryClient } from '@tanstack/vue-query';
import { getGetUsersMeQueryKey } from '@/lib/api/users/users';
import {
  RestaurantDetailsFields,
  RestaurantHoursFields,
} from '@/components/(manage)/onboarding/fields';
import HoursFormTip from '@/components/(manage)/onboarding/HoursFormTip.vue';
import { Alert, AlertDescription, AlertTitle } from '@/components/ui/alert';
import type { ProblemDetailResponse } from '@/lib/api-model';

useHead({
  title: 'Complete restaurant profile',
});

const router = useRouter();
const { user, signOut } = useRestaurantUser();
const { address, setPlaceId } = useAddress();
const errorMessage = ref('');

const { current, goToPrevious, goToNext, isCurrent, isFirst, isLast } = useStepper({
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

const queryClient = useQueryClient();

const onSubmit = form.handleSubmit(async (formValues) => {
  const requestData = mapRestaurantDataToDTO(formValues);
  const res = await postRestaurants(requestData);

  if (res.status === 200) {
    errorMessage.value = '';
    await queryClient.invalidateQueries({
      queryKey: getGetUsersMeQueryKey(),
    });
    await queryClient.invalidateQueries({
      queryKey: getRestaurantUserQueryKey(),
    });
    await router.push('/manage');
  } else if (res.status === 400) {
    const { detail } = res.data as unknown as ProblemDetailResponse;

    if (detail) {
      errorMessage.value = detail;
    }
  }
});
</script>

<template>
  <div class="grid min-h-svh grid-cols-2">
    <div class="border-r border-neutral-300">
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

      <form @submit="onSubmit">
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
          <AddressAutocompleteInput
            popover-class="md:w-80 lg:w-full lg:max-w-prose"
            @on-place-select="setPlaceId"
          />

          <Alert variant="destructive" v-if="errorMessage" class="my-4 bg-red-100">
            <AlertCircleIcon class="h-4 w-4" />
            <AlertTitle>Failed to register your restaurant</AlertTitle>
            <AlertDescription>
              {{ errorMessage }}
            </AlertDescription>
          </Alert>
        </div>

        <div class="mt-8 flex gap-2">
          <Button type="button" :disabled="isFirst" variant="outline" @click="goToPrevious">
            <ChevronLeftIcon class="mr-2 h-4 w-4" /> Back
          </Button>
          <Button type="button" @click="goToNext" v-if="!isLast">
            Continue <ChevronRightIcon class="ml-2 h-4 w-4" />
          </Button>
          <Button v-else type="submit" :disabled="!form.meta.value.valid">Save</Button>
        </div>
      </form>
    </div>
  </div>
</template>

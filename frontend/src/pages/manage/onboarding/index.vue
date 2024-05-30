<script setup lang="ts">
import { useRestaurantUser } from '@/composables/useRestaurantUser';
import { useHead } from '@unhead/vue';
import { Button } from '@/components/ui/button';
import { Input } from '@/components/ui/input';
import { watch } from 'vue';
import { toTypedSchema } from '@vee-validate/zod';
import { restaurantSchema } from '@/components/(manage)/onboarding/restaurantSchema';
import { useForm } from 'vee-validate';
import { FormControl, FormField, FormItem, FormLabel, FormMessage } from '@/components/ui/form';
import { useStepper } from '@vueuse/core';
import { cn } from '@/lib/utils';
import { ChevronLeftIcon, ChevronRightIcon, ClockIcon, LogOutIcon } from 'lucide-vue-next';
import Logo from '@/components/Logo.vue';
import { Alert, AlertDescription, AlertTitle } from '@/components/ui/alert';
import AddressAutocompleteInput from '@/components/maps/autocomplete/AddressAutocompleteInput.vue';
import { useAddress } from '@/composables/maps/useAddress';
import { postRestaurants } from '@/lib/api/restaurants/restaurants';
import type { HoursDTO } from '@/lib/api-model';
import { useRouter } from 'vue-router/auto';

useHead({
  title: 'Complete restaurant profile',
});

const router = useRouter();
const { user, restaurant, isProfileComplete, signOut } = useRestaurantUser();
const { address, setPlaceId } = useAddress();

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

const formFields = {
  details: [
    {
      label: 'Restaurant name',
      fieldName: 'details.name',
    },
    {
      label: 'Phone number',
      fieldName: 'details.phoneNumber',
    },
  ],
  hours: [
    {
      label: 'Monday',
      fieldName: 'hours.monday',
      fields: ['Open', 'Close'],
    },
    {
      label: 'Tuesday',
      fieldName: 'hours.tuesday',
      fields: ['Open', 'Close'],
    },
    {
      label: 'Wednesday',
      fieldName: 'hours.wednesday',
      fields: ['Open', 'Close'],
    },
    {
      label: 'Thursday',
      fieldName: 'hours.thursday',
      fields: ['Open', 'Close'],
    },
    {
      label: 'Friday',
      fieldName: 'hours.friday',
      fields: ['Open', 'Close'],
    },
    {
      label: 'Saturday',
      fieldName: 'hours.saturday',
      fields: ['Open', 'Close'],
    },
    {
      label: 'Sunday',
      fieldName: 'hours.sunday',
      fields: ['Open', 'Close'],
    },
  ],
};

const formSchema = toTypedSchema(restaurantSchema);
const form = useForm({
  validationSchema: formSchema,
  initialValues: {
    details: {
      name: 'Laxmi',
      phoneNumber: '123456789',
    },
    hours: {
      monday: ['10:00', '20:00'],
      tuesday: ['10:00', '20:00'],
      thursday: ['10:00', '20:00'],
      wednesday: ['10:00', '20:00'],
      friday: ['10:00', '20:00'],
      saturday: ['12:00', '22:00'],
      sunday: ['', ''],
    },
  },
});

watch(address, () => {
  if (address.value) {
    form.setValues({
      ...form.values,
      address: address.value,
    });
  }
});

watch(restaurant, () => {
  console.log(restaurant.value);
});

watch(user, () => {
  console.log(user.value);
});

const onSubmit = form.handleSubmit(async (formValues) => {
  if (!user.value || !address.value) {
    return;
  }

  const sanitizeHours = (hours: string[]) => {
    return hours.filter((hour) => hour !== '');
  };
  const mappedHours: HoursDTO = Object.entries(formValues.hours).reduce((acc, [key, value]) => {
    acc[key as keyof HoursDTO] = sanitizeHours(value);
    return acc;
  }, {} as HoursDTO);

  const res = await postRestaurants({
    name: formValues.details.name,
    phoneNumber: formValues.details.phoneNumber,
    hours: mappedHours,
    userId: user.value.id,
    addressInputDTO: formValues.address,
  });

  if (res.status === 200) {
    await router.push('/manage');
  } else if (res.status === 400) {
    // handle
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
          <FormField
            v-for="{ label, fieldName } in formFields.details"
            :key="fieldName"
            v-slot="{ componentField }"
            :name="fieldName"
          >
            <FormItem>
              <FormLabel>{{ label }}</FormLabel>
              <FormControl>
                <Input type="text" v-bind="componentField" />
              </FormControl>
              <FormMessage />
            </FormItem>
          </FormField>
        </div>

        <!-- Hours -->
        <div :class="cn('hidden', isCurrent('hours') && 'block')">
          <Alert class="mb-3 bg-secondary/25">
            <ClockIcon class="h-4 w-4" />
            <AlertTitle>How to fill the form?</AlertTitle>
            <AlertDescription>
              <ul class="ml-4 list-disc">
                <li>Use HH:MM 24-hour format,</li>
                <li>If restaurant is closed on a given day, leave both fields empty.</li>
              </ul>
            </AlertDescription>
          </Alert>
        </div>
        <div :class="cn('hidden max-w-80', isCurrent('hours') && 'block')">
          <div class="grid grid-cols-[max-content,1fr,1fr] gap-2 gap-x-4">
            <p class="col-start-2 text-sm font-medium">Open</p>
            <p class="text-sm font-medium">Close</p>

            <template v-for="{ label, fieldName } in formFields.hours" :key="fieldName">
              <p aria-hidden="true" class="h-fit self-center">
                {{ label }}
              </p>

              <FormField v-slot="{ componentField }" :name="`${fieldName}[0]`">
                <FormItem class="space-y-0">
                  <FormLabel class="sr-only">Open</FormLabel>
                  <FormControl>
                    <Input type="text" v-bind="componentField" />
                  </FormControl>
                  <FormMessage />
                </FormItem>
              </FormField>
              <FormField v-slot="{ componentField }" :name="`${fieldName}[1]`">
                <FormItem class="space-y-0">
                  <FormLabel class="sr-only">Close</FormLabel>
                  <FormControl>
                    <Input type="text" v-bind="componentField" />
                  </FormControl>
                  <FormMessage />
                </FormItem>
              </FormField>
            </template>
          </div>
        </div>

        <!-- Location -->
        <div :class="cn('hidden', isCurrent('location') && 'block')">
          <AddressAutocompleteInput
            popover-class="md:w-80 lg:w-full lg:max-w-prose"
            @on-place-select="setPlaceId"
          />
        </div>

        <div class="mt-8 flex gap-2">
          <Button type="button" :disabled="isFirst" variant="outline" @click="goToPrevious">
            <ChevronLeftIcon class="mr-2 h-4 w-4" /> Back
          </Button>
          <Button type="button" @click="goToNext" v-if="!isLast">
            Continue <ChevronRightIcon class="ml-2 h-4 w-4" />
          </Button>
          <Button v-else type="submit">Save</Button>
        </div>
      </form>
    </div>
  </div>
</template>

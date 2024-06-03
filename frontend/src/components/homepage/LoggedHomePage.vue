<script setup lang="ts">
import { useHead } from '@unhead/vue';
import { useUser } from '@/composables/useUser';

import FavouritesDisplay from '@/components/homepage/FavouritesDisplay.vue';
import WelcomeComponent from '@/components/homepage/WelcomeComponent.vue';
import LatestBookings from '@/components/homepage/LatestBookings.vue';
import { useGetBookings } from '@/lib/api/bookings/bookings';
import { computed, unref } from 'vue';

useHead({
  title: 'Home',
});

const { user } = useUser();

const userId = computed(() => user.value?.id ?? '');

const { data } = useGetBookings({ userId: userId.value });
const bookings = computed(() => unref(data)?.data ?? []);
</script>

<template>
  <div class="mx-auto w-full px-4 sm:max-w-screen-sm md:max-w-screen-md lg:max-w-screen-xl">
    <WelcomeComponent v-if="user?.id" :user="user" />
    <LatestBookings v-if="user?.id && bookings.length > 0" :bookings="bookings" />
    <FavouritesDisplay v-if="user?.id" :userId="user.id" />
  </div>
</template>

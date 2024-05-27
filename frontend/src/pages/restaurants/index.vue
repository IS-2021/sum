<route lang="yaml">
meta:
  layout: restaurants
</route>

<script setup lang="ts">
import { useHead } from '@unhead/vue';
import { useGetRestaurants } from '@/lib/api/restaurants/restaurants';
import { computed, ref, unref } from 'vue';
import { useUser } from '@/composables/useUser';
import { Button } from '@/components/ui/button';
import MapBrowser from '@/components/restaurants/views/MapBrowser.vue';
import ListBrowser from '@/components/restaurants/views/ListBrowser.vue';
import { ChevronLeft, MapIcon } from 'lucide-vue-next';

useHead({
  title: 'Restaurants',
});

const isMapView = ref(false);
const toggleView = () => {
  isMapView.value = !isMapView.value;
};

const { user } = useUser();
const { data, isPending: areRestaurantsLoading } = useGetRestaurants();
const restaurants = computed(() => unref(data)?.data);
</script>

<template>
  <template v-if="areRestaurantsLoading">
    <p>Loading...</p>
  </template>
  <template v-else-if="restaurants">
    <div v-if="isMapView" class="flex flex-grow relative">
      <Button
        @click="toggleView"
        class="absolute shadow top-3 rounded-full pr-6 z-50 left-1/2 -translate-x-1/2"
      >
        <ChevronLeft class="h-4 v-4 mr-2" />
        Back to list
      </Button>

      <MapBrowser :restaurants="restaurants" />
    </div>

    <div v-else class="px-4 sm:container">
      <div class="flex flex-col sm:flex-row sm:items-center gap-2 justify-between mt-8 mb-6">
        <h1 class="font-bold text-2xl tracking-tight">
          Restaurants {{ user?.address && `in ${user.address.city}` }}
        </h1>

        <Button @click="toggleView"> <MapIcon class="mr-3" /> Browse on a map </Button>
      </div>

      <div>
        <ListBrowser :restaurants="restaurants" />
      </div>
    </div>
  </template>
</template>

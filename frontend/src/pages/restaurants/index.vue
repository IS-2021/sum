<route lang="yaml">
meta:
  layout: empty
</route>

<script setup lang="ts">
import { useHead } from '@unhead/vue';
import { ref } from 'vue';
import { useUser } from '@/composables/useUser';
import { Button } from '@/components/ui/button';
import MapBrowser from '@/components/restaurants/views/MapBrowser.vue';
import ListBrowser from '@/components/restaurants/views/ListBrowser.vue';
import { ChevronLeft, MapIcon } from 'lucide-vue-next';
import Header from '@/components/navbar/Header.vue';
import Footer from '@/components/Footer.vue';
import { cn } from '@/lib/utils';
import { useRestaurants } from '@/components/restaurants/useRestaurants';
import RadiusSelect from '@/components/restaurants/RadiusSelect.vue';
import AddressAutocompleteInput from '@/components/maps/autocomplete/AddressAutocompleteInput.vue';
import { useAddress } from '@/composables/maps/useAddress';

useHead({
  title: 'Restaurants - Food Good',
});

const isMapView = ref(false);
const toggleView = () => {
  isMapView.value = !isMapView.value;
};

const radius = ref<number>(5);

const { user } = useUser();
const { address, setPlaceId } = useAddress();
const { restaurants } = useRestaurants({
  radius,
  overrideAddress: address,
});

function setRadius(value: string) {
  radius.value = parseInt(value);
}
</script>

<template>
  <Header />
  <div :class="cn('flex flex-grow', !isMapView && 'my-8')">
    <template v-if="restaurants">
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

      <div v-else class="px-4 sm:container w-full">
        <div class="flex flex-col sm:flex-row sm:items-center gap-2 justify-between mb-6">
          <h1 class="font-bold text-2xl tracking-tight">
            Restaurants {{ user?.address && `in ${user.address.city}` }}
          </h1>

          <div class="flex gap-2">
            <AddressAutocompleteInput @on-place-select="setPlaceId" />
            <RadiusSelect @update:radius="setRadius" />
            <Button @click="toggleView"> <MapIcon class="mr-3" /> Browse on a map </Button>
          </div>
        </div>

        <div>
          <ListBrowser :restaurants="restaurants" />
        </div>
      </div>
    </template>
  </div>
  <Footer v-if="!isMapView" />
</template>

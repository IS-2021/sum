<route lang="yaml">
meta:
  layout: restaurants
</route>

<script setup lang="ts">
import { useHead } from '@unhead/vue';
import { useGetRestaurants } from '@/lib/api/restaurants/restaurants';
import { computed, unref } from 'vue';
import { useUser } from '@/composables/useUser';
import { Tabs, TabsContent, TabsList, TabsTrigger } from '@/components/ui/tabs';
import MapBrowser from '@/components/restaurants/views/MapBrowser.vue';
import ListBrowser from '@/components/restaurants/views/ListBrowser.vue';

useHead({
  title: 'Restaurants',
});

const { user } = useUser();
const { data, isPending: areRestaurantsLoading } = useGetRestaurants();
const restaurants = computed(() => unref(data)?.data);
</script>

<template>
  <template v-if="areRestaurantsLoading">
    <p>Loading...</p>
  </template>
  <template v-else-if="restaurants">
    <Tabs default-value="list" class="mt-4">
      <TabsList class="grid gap-2 grid-cols-2 px-4 sm:container">
        <TabsTrigger value="list" class="data-[state=active]:bg-primary"> List </TabsTrigger>
        <TabsTrigger value="map" class="data-[state=active]:bg-primary"> Map </TabsTrigger>
      </TabsList>

      <TabsContent value="list">
        <div class="px-4 sm:container">
          <ListBrowser :restaurants="restaurants" />
        </div>
      </TabsContent>
      <TabsContent value="map">
        <MapBrowser :restaurants="restaurants" />
      </TabsContent>
    </Tabs>
  </template>
</template>

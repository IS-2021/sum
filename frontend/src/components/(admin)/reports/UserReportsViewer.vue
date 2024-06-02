<script setup lang="ts">
import { Tabs, TabsContent, TabsList, TabsTrigger } from '@/components/ui/tabs';
import ReportCard from '@/components/(manage)/dashboard/ReportCard.vue';
import type { ReportDTO, Uuid } from '@/lib/api-model';

interface RestaurantReportsViewerProps {
  reportsFromUser: ReportDTO[];
  reportsAboutUser: ReportDTO[];
  showClosedReports: boolean;
}

withDefaults(defineProps<RestaurantReportsViewerProps>(), {
  showClosedReports: true,
});

const emits = defineEmits<{
  (e: 'banRestaurant', reportId: Uuid): void;
  (e: 'banUser', reportId: Uuid): void;
  (e: 'closeReport', reportId: Uuid, reportAbout: 'restaurant' | 'user'): void;
}>();

function banRestaurantHandler(reportId: Uuid) {
  emits('banRestaurant', reportId);
}
function banUserHandler(reportId: Uuid) {
  emits('banUser', reportId);
}
function closeReportHandler(reportId: Uuid, reportAbout: 'restaurant' | 'user') {
  emits('closeReport', reportId, reportAbout);
}
</script>

<template>
  <Tabs default-value="reportsFromUser">
    <TabsList class="grid w-full grid-cols-2">
      <TabsTrigger value="reportsFromUser"> By user </TabsTrigger>
      <TabsTrigger value="reportsAboutUser"> By restaurants </TabsTrigger>
    </TabsList>
    <TabsContent value="reportsFromUser">
      <ul v-if="reportsFromUser.length > 0" class="space-y-2">
        <li v-for="report in reportsFromUser" :key="report.id">
          <ReportCard
            @ban-user="banUserHandler"
            @ban-restaurant="banRestaurantHandler"
            @close-report="(reportId) => closeReportHandler(reportId, 'restaurant')"
            :report="report"
            :showActionButtons="report.isOpen"
            :actions="['banRestaurant', 'closeReport', 'viewRestaurant']"
          />
        </li>
      </ul>
      <p v-else class="mt-4">This user hasn't reported any restaurant.</p>
    </TabsContent>
    <TabsContent value="reportsAboutUser">
      <ul v-if="reportsAboutUser.length > 0">
        <li v-for="report in reportsAboutUser" :key="report.id">
          <ReportCard
            @ban-user="banUserHandler"
            @ban-restaurant="banRestaurantHandler"
            @close-report="(reportId) => closeReportHandler(reportId, 'user')"
            :report="report"
            :showActionButtons="report.isOpen"
            :actions="['banUser', 'closeReport', 'viewRestaurant']"
          />
        </li>
      </ul>
      <p v-else class="mt-4">This user hasn't been reported by any restaurant.</p>
    </TabsContent>
  </Tabs>
</template>

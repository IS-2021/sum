<route lang="yaml">
meta:
  layout: admin
</route>

<script setup lang="ts">
import { useHead } from '@unhead/vue';
import { useRoute } from 'vue-router/auto';
import { useGetAdminUsersId } from '@/lib/api/admin-users/admin-users';
import { computed } from 'vue';
import SettingsSection from '@/components/(manage)/settings/SettingsSection.vue';
import { MailIcon, PhoneIcon, UserRoundIcon } from 'lucide-vue-next';
import { Separator } from '@/components/ui/separator';
import { Button } from '@/components/ui/button';
import UserStatusIcon from '@/components/(admin)/users/UserStatusIcon.vue';
import ConfirmDialog from '@/components/dialogs/ConfirmDialog.vue';
import { useReportsAboutUser } from '@/components/(admin)/composables/useReportsAboutUser';
import { useReportsFromUser } from '@/components/(admin)/composables/useReportsFromUser';
import UserReportsViewer from '@/components/(admin)/reports/UserReportsViewer.vue';
import { closeReportAboutRestaurant, closeReportAboutUser } from '@/components/(admin)/reports/api';
import { useQueryClient } from '@tanstack/vue-query';

useHead({
  title: 'User',
});

const route = useRoute('/admin/restaurants/[id]');
const userId = route.params.id;

const queryClient = useQueryClient();

const { data, isLoading } = useGetAdminUsersId(userId);
const user = computed(() => data.value?.data);

const { reportsAboutUser } = useReportsAboutUser(userId);
const { reportsFromUser } = useReportsFromUser(userId);

async function handleUnbanUser() {}

async function handleBanRestaurant(reportId: string) {
  const res = await closeReportAboutRestaurant(reportId, true);

  if (res.status === 200) {
    await queryClient.invalidateQueries();
  }
}

async function handleBanUser(reportId: string) {
  const res = await closeReportAboutUser(reportId, true);

  if (res.status === 200) {
    await queryClient.invalidateQueries();
  }
}

async function handleCloseReport(reportId: string, reportAbout: 'user' | 'restaurant') {
  if (reportAbout === 'restaurant') {
    const res = await closeReportAboutRestaurant(reportId, false);

    if (res.status === 200) {
      await queryClient.invalidateQueries();
    }
  } else if (reportAbout === 'user') {
    const res = await closeReportAboutUser(reportId, false);

    if (res.status === 200) {
      await queryClient.invalidateQueries();
    }
  }
}
</script>

<template>
  <div v-if="isLoading">
    <p>Loading</p>
  </div>

  <div v-if="user">
    <h1 class="mb-10 text-2xl font-semibold tracking-tight">
      {{ user.firstName }} {{ user.secondName }}
    </h1>

    <SettingsSection>
      <h2 class="text-lg font-semibold tracking-tight">Profile details</h2>
      <Separator class="mb-4 mt-2" />

      <div class="flex items-center justify-between">
        <p v-if="user.blocked">
          <UserStatusIcon :is-banned="user.blocked" class="h-4 w-4 mr-2 inline-block" />
          <template v-if="user.blocked">
            <span class="text-red-500">Account banned</span>
          </template>
          <template v-else>
            <span class="text-primary">Account active</span>
          </template>
        </p>

        <ConfirmDialog v-if="user.blocked">
          <template v-slot:trigger>
            <Button variant="secondary"> Unban </Button>
          </template>
          <template v-slot:description>
            <p>Are you sure you want to unban this user?</p>
          </template>
          <template v-slot:confirmButton>
            <Button @click="handleUnbanUser" variant="destructive"> Unban </Button>
          </template>
        </ConfirmDialog>
      </div>

      <div>
        <ul class="space-y-1.5">
          <li><UserRoundIcon class="mr-2 inline-block h-4 w-4" />Username: {{ user.username }}</li>
          <li><PhoneIcon class="mr-2 inline-block h-4 w-4" />Telephone: {{ user.phoneNumber }}</li>
          <!--        <li>-->
          <!--          <MapPinIcon class="mr-2 inline-block h-4 w-4" />Address: {{ formatAddress(user.address) }}-->
          <!--        </li>-->
          <li><MailIcon class="mr-2 inline-block h-4 w-4" />Email: {{ user.email }}</li>
        </ul>
      </div>
    </SettingsSection>

    <SettingsSection>
      <h2 class="text-lg font-semibold tracking-tight">Reports</h2>
      <Separator class="mb-4 mt-2" />

      <UserReportsViewer
        @ban-restaurant="handleBanRestaurant"
        @ban-user="handleBanUser"
        @close-report="handleCloseReport"
        :show-closed-reports="true"
        :reports-from-user="reportsFromUser"
        :reports-about-user="reportsAboutUser"
      />
    </SettingsSection>
  </div>
</template>

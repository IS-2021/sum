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

useHead({
  title: 'User',
});

const route = useRoute('/admin/restaurants/[id]');
const userId = route.params.id;

const { data, isLoading } = useGetAdminUsersId(userId);
const user = computed(() => data.value?.data);

async function handleUnbanUser() {}
</script>

<template>
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

        <Button @click="handleUnbanUser" v-if="user.blocked" variant="secondary"> Unban </Button>
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
  </div>
</template>

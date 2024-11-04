import axios from 'axios';
import authHeader from './auth-header';

const SYNC_API_BASE_URL = "http://localhost:8090"
class SyncService {
    activateSyncJob() {
        return axios.put(SYNC_API_BASE_URL + '/job/scheduler/activate', { headers: authHeader() });
    }

    deactivateSyncJob() {
        return axios.put(SYNC_API_BASE_URL + '/job/scheduler/deactivate', { headers: authHeader() });
    }
}

export default new SyncService();
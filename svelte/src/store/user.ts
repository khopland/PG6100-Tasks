import { writable } from 'svelte/store';
import Fetch from '$lib/fetch';

const { subscribe: sub, set } = writable(null);
export const subscribe = sub;
export const signout = () => set(null);
export const signin = (state) => set(state);

export const fetchAndUpdateUserInfo = async () => {
	const response = await Fetch('/api/auth/user');
	if (response.status === 401) {
		//that is ok
		signout();
		return;
	}
	if (response.status === 200) signin(await response.json());
};

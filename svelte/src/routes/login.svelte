<script lang="ts">
	import Fetch from '$lib/fetch';
	import { fetchAndUpdateUserInfo } from '../store/user';
	import { goto } from '$app/navigation';

	let userId = '';
	let password = '';
	let errorMsg = null;

	const submit = async () => {
		let res;
		try {
			res = await Fetch('/api/auth/login', {
				method: 'post',
				headers: {
					'Content-Type': 'application/json'
				},
				body: JSON.stringify({ userId, password })
			});
		} catch (err) {
			errorMsg = 'Failed to connect to server: ' + err;
			return;
		}

		if (res.status === 401) {
			errorMsg = 'Invalid userId/password';
			return;
		}

		if (res.status !== 204) {
			errorMsg = 'Error when connecting to server: status code ' + res.status;
			return;
		}

		errorMsg = null;
		await fetchAndUpdateUserInfo();
		await goto('/');
	};
</script>

<div>
	{#if errorMsg}
		<div>
			<p>{errorMsg}</p>
		</div>
	{/if}
	<form on:submit|preventDefault={submit}>
		<label for="username">username</label>
		<input type="text" id="username" bind:value={userId} required />
		<label for="password">password</label>
		<input type="password" id="password" bind:value={password} required />
		<button>log in</button>
	</form>
	<a href="/signup">sign up</a>
</div>

<script lang="ts">
	import Fetch from '$lib/fetch';
	import { fetchAndUpdateUserInfo } from '../store/user';
	import { goto } from '$app/navigation';

	let userId = '';
	let password = '';
	let confirm = '';
	let errorMsg = null;

	const submit = async () => {
		if (confirm !== password) {
			errorMsg = 'Passwords do not match';
			return;
		}
		let res;
		try {
			res = await Fetch('/api/auth/signUp', {
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

		if (res.status !== 201) {
			errorMsg = 'Error when connecting to server: status code ' + res.status;
			return;
		}

		errorMsg = null;
		await fetchAndUpdateUserInfo();
		await goto('/');
	};
</script>

<div class="container">
	{#if errorMsg}
		<div>
			<p>{errorMsg}</p>
		</div>
	{/if}

	<form on:submit|preventDefault={submit} class="form">
		<label for="username">username</label>
		<input type="text" id="username" bind:value={userId} required />
		<label for="password">password</label>
		<input type="password" id="password" bind:value={password} required />
		<label for="confirm">confirm password</label>
		<input type="password" id="confirm" bind:value={confirm} required />
		<button>sign up</button>
	</form>
	<a href="/login">login</a>
</div>

<style>
	.container {
		display: flex;
		flex-direction: column;
		padding-left: 5vw;
	}
	.form {
		display: flex;
		flex-direction: column;
		width: 30%;
	}
</style>

#include <stdlib.h>
#include <stdio.h>
#include <sys/types.h>
#include <sys/wait.h>
#include <unistd.h>
#include <signal.h>
#include <errno.h>
#include <string.h>
#include <sys/ipc.h>
#include <sys/shm.h>

#define BUFFOR_SIZE 80
int MASTER_ID = -1;


void on_usr1(int signal) {
}

int main(int argc, char** argv)
{
    	pid_t pid = getpid();
	pid_t pid_p = getpid();
	printf("PID: %d\n", pid);
	int id;
	sigset_t mask; /* Maska sygna³ów */
	struct sigaction usr1;
	
	int i = 0;
	int j = 0;
	int k = 0;
	int n = atoi(argv[1]); /*liczba procesów potomnych*/
	int* tab = (int*) malloc (n * sizeof tab); /*tablica z PID-ami procesów potomnych*/

			int shmid1 = shmget(pid_p, BUFFOR_SIZE*sizeof(int), IPC_CREAT|0600); /*pamiêæ dla vektora*/
			if (shmid1 == -1){
				perror("B³¹d przy wektorze");
				exit(1);
			}

			int shmid2 = shmget(pid_p+1, 2*n*sizeof(int), IPC_CREAT|0600); /*pamiêæ dla zakresów*/
			if (shmid2 == -1){
				perror("B³¹d przy zakresach");
				exit(1);
			}

			int shmid3 = shmget(pid_p+2, n*sizeof(int), IPC_CREAT|0600); /*pamiêæ dla sum*/
			if (shmid3 == -1){
				perror("B³¹d przy sumach");
				exit(1);
			}
	
while(i < n){
MASTER_ID++;
	switch(pid = fork()){
		case -1:
			fprintf(stderr, "Blad w fork\n");
			return EXIT_FAILURE;
			
		case 0: 
			i = n; /*proces potomny wychodzi poza pêtle*/
			id = MASTER_ID;

			/* Konfiguracja obs³ugi sygna³u USR1 */			
			sigemptyset(&mask); /* Wyczyœæ maskê */
			usr1.sa_handler = (&on_usr1);
			usr1.sa_mask = mask;
			usr1.sa_flags = SA_SIGINFO;
			sigaction(SIGUSR1, &usr1, NULL);

			pause();
			/*printf ("ID: %d\n", id);*/


			int* vect= (int*)shmat(shmid1, NULL, 0);
			if (vect == NULL){
				perror("Przylaczenie segmentu pamieci wspoldzielonej");
				exit(1);
			}


			int* range = (int*)shmat(shmid2, NULL, 0);
			if (range == NULL){
				perror("Przylaczenie segmentu pamieci wspoldzielonej");
				exit(1);
			}


			int* res = (int*)shmat(shmid3, NULL, 0);
			if (res == NULL){
				perror("Przylaczenie segmentu pamieci wspoldzielonej");
				exit(1);
			}
			
			for ( k = range[2*id]; k <= range[2*id+1]; k++){
				res[id] += vect[k];
			}			
			break;
			
		default:
			tab[i] = pid; /*zapisuje PID procesu potomnego*/
			i++; /*proces macierzysty iteruje dalej*/
			
			/*gdy ostatnia iteracja czekaj na sygna³y*/
			if(i == n){
				sigemptyset(&mask); /* Wyczyœæ maskê */
				sigprocmask(SIG_BLOCK, &mask, NULL); /* Ustaw maskê dla ca³ego procesu */

			
				FILE* f = fopen("vector.dat", "r");
				char buffor[BUFFOR_SIZE+1];
				int iv;
				int nv;
				int shmid1 = shmget(pid_p, BUFFOR_SIZE*sizeof(int), IPC_CREAT|0600); /*pamiêæ dla vektora*/
				if (shmid1 == -1){
					perror("B³¹d przy wektorze");
					exit(1);
				}
				int* vect= (int*)shmat(shmid1, NULL, 0);
				if (vect == NULL){
					perror("Przylaczenie segmentu pamieci wspoldzielonej");
					exit(1);
				}

				fgets(buffor, BUFFOR_SIZE, f);
				nv = atoi(buffor);
				printf("Vector has %d elements\n", nv);
				for(iv = 0; iv < nv; iv++) {
					fgets(buffor, BUFFOR_SIZE, f);
					vect[iv] = atof(buffor);
				}
				fclose(f);
				printf("Wektor:\n");
				for(iv = 0; iv < nv; iv++) {
					printf("%d\n", vect[iv]);
				}

				int shmid2 = shmget(pid_p+1, 2*n*sizeof(int), IPC_CREAT|0600); /*pamiêæ dla zakresów*/
				if (shmid2 == -1){
					perror("B³¹d przy zakresach");
					exit(1);
				}
				int* range = (int*)shmat(shmid2, NULL, 0);
				if (range == NULL){
					perror("Przylaczenie segmentu pamieci wspoldzielonej");
					exit(1);
				}
	
				int subvect_count = nv/n;
				for(iv = 0; iv < n; iv++) {
					range[2*iv] = iv * subvect_count;
					range[2*iv+1] = (iv + 1)*subvect_count - 1;
					if (iv + 1 == n){
						range[2*iv+1] += nv%n;
					}
				}

				printf("Zakresy:\n");			
				for(iv = 0; iv < 2*n; iv++) {
					printf("%d\n", range[iv]);
				}

				int shmid3 = shmget(pid_p+2, n*sizeof(int), IPC_CREAT|0600); /*pamiêæ dla sum*/
				if (shmid3 == -1){
					perror("B³¹d przy sumach");
					exit(1);
				}
				int* res = (int*)shmat(shmid3, NULL, 0);
				if (res == NULL){
					perror("Przylaczenie segmentu pamieci wspoldzielonej");
					exit(1);
				}

				for(iv = 0; iv < n; iv++) {
					res[iv] = 0;
				}
				
				printf("Sumy cz¹stkowe:\n");
				for(iv = 0; iv < n; iv++) {
					printf("%d\n", res[iv]);
				}
				sleep(1);
				for (iv = 0; iv < n; iv++){
					kill(tab[iv], SIGUSR1);
				}

				wait(0);
				
				printf("Sumy cz¹stkowe po obliczeniach:\n");
				for(iv = 0; iv < n; iv++) {
					printf("%d\n", res[iv]);
				}
			
				int sum = 0;
				for(iv = 0; iv < n; iv++) {
					sum += res[iv];
				}
				printf("Suma: %d\n", sum);

			}
			break;
			
	}
}
	return EXIT_SUCCESS;
}

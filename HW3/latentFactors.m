% P=[
% 0.93 0.66 0.84
%  0.23 1.14 0.89
%  0.91 0.27 1.00
%  1.07 0.25 0.58];
% 
% Q=[  0.72 1.13 0.49
%  1.02 0.36 0.46
%  0.19 0.19 1.00];

numFactors = 20;

P=rand(1682,numFactors);
Q=rand(943,numFactors);

learningRate = .01;
regularizationFactor = .2;
newQi=zeros(numFactors);
newPi=zeros(numFactors);
iterations = 40;
sanity = ratings_train;
for iteration=1:iterations
    fid = fopen('ratings.train.txt');
    
    tline = fgets(fid);
    while ischar(tline)
       % disp(tline)
   % for i=1:size(sanity,1)
   splitted = strsplit(tline,'\t');
%             centroid =[];
%             for j=1:size(splitted,2)
%                 centroid = [centroid str2num(char(splitted(1,j)))];
%             end
%         currentUserId = sanity(i,1);
%         currentMovieId= sanity(i,2);
%         currentRating = sanity(i,3);
        currentUserId = str2num(char(splitted(1)));
        currentMovieId= str2num(char(splitted(2)));
        currentRating = str2num(char(splitted(3)));

        qipx=Q(currentUserId,:)*transpose(P(currentMovieId,:));
        errorDer = 2*(currentRating - qipx);
        
        newQi = Q(currentUserId,:) + learningRate *(errorDer * P(currentMovieId,:) - ...
            regularizationFactor * Q(currentUserId,:));
        
        newPi = P(currentMovieId,:) + learningRate *(errorDer * Q(currentUserId,:) - ...
            regularizationFactor * P(currentMovieId,:));
        Q(currentUserId,:) = newQi;
        P(currentMovieId,:) = newPi;
                    tline = fgets(fid);
    end
            fclose(fid);

    error = 0;
    for i=1:size(sanity,1)
        currentUserId = sanity(i,1);
        currentMovieId= sanity(i,2);
        currentRating = sanity(i,3);
        error = error + (currentRating - Q(currentUserId,:)*transpose(P(currentMovieId,:)))^2;
    end
    
    error = error + regularizationFactor * ( norm(P)^2+norm(Q)^2);
    disp(error)
end
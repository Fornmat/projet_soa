CREATE TABLE requests (
    id INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description TEXT NOT NULL,
    requester_id INT NOT NULL,
    helper_id INT,
    desired_date DATETIME NOT NULL,
    status ENUM('ATTENTE', 'EN_COURS', 'REALISEE', 'ABANDONNEE', 'FERMEE') NOT NULL,
    keywords VARCHAR(255),
    FOREIGN KEY (requester_id) REFERENCES users(id),
    FOREIGN KEY (helper_id) REFERENCES users(id)
);

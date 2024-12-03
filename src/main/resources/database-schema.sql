CREATE TABLE `students` (
                            `id` int(11) NOT NULL AUTO_INCREMENT,
                            `firstname` varchar(45) DEFAULT NULL,
                            `lastname` varchar(45) DEFAULT NULL,
                            `email` varchar(45) DEFAULT NULL,
                            PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=latin1;

INSERT INTO `students` VALUES
                           (1,'David','Adams','david@luv2code.com'),
                           (2,'John','Doe','john@luv2code.com'),
                           (3,'Ajay','Rao','ajay@luv2code.com'),
                           (4,'Mary','Public','mary@luv2code.com'),
                           (5,'Maxwell','Dixon','max@luv2code.com');

CREATE TABLE `math_grades` (
                               `id` int(11) NOT NULL AUTO_INCREMENT,
                               `student_id` int(11) DEFAULT NULL,
                               `grade` double(5, 2) DEFAULT NULL,
                               PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=latin1;


INSERT INTO `math_grades` VALUES
                              (1, 1, 80),
                              (2, 1, 90),
                              (3, 1, 72);


CREATE TABLE `science_grades` (
                                  `id` int(11) NOT NULL AUTO_INCREMENT,
                                  `student_id` int(11) DEFAULT NULL,
                                  `grade` double(5, 2) DEFAULT NULL,
                                  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=latin1;

INSERT INTO `science_grades` VALUES
                                 (1, 1 , 80),
                                 (2, 1, 90),
                                 (3, 1 , 72);


CREATE TABLE `history_grades` (
                                  `id` int(11) NOT NULL AUTO_INCREMENT,
                                  `student_id` int(11) DEFAULT NULL,
                                  `grade` double(5, 2) DEFAULT NULL,
                                  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=latin1;

INSERT INTO `history_grades` VALUES
                                 (1, 1 , 80),
                                 (2, 1, 90),
                                 (3, 1 , 72);

